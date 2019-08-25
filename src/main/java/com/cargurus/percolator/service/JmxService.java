package com.cargurus.percolator.service;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PreDestroy;
import javax.management.Attribute;
import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.cargurus.percolator.domain.Application;
import com.cargurus.percolator.domain.JmxAttribute;
import com.cargurus.percolator.domain.JmxBean;
import com.cargurus.percolator.domain.JmxOperation;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

@Component
public class JmxService {

    private static final Duration PING_TIMEOUT = Duration.ofSeconds(1);

    private static final Map<String, Class> CONVERTABLE_TYPES = Stream.of(
            boolean.class, Boolean.class,
            int.class, Integer.class,
            long.class, Long.class,
            float.class, Float.class,
            double.class, Double.class,
            String.class).
        collect(toMap(Class::getName, c -> c));

    private final Logger log = LoggerFactory.getLogger(JmxService.class);

    private final ConcurrentMap<Application, JMXConnector> connections = new ConcurrentHashMap<>();

    private final ConversionService conversionService;

    public JmxService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public Map<String, List<JmxBean>> listBeans(Application app) {
        return listBeans(app, null);
    }

    public Map<String, List<JmxBean>> listBeans(Application app, String domain) {
        return executeWithConnection(app, conn -> {
            ObjectName objectName = null;
            if (domain != null) {
                objectName = new ObjectName(domain + ":*");
            }
            return conn.queryNames(objectName, null).stream().
                map(JmxBean::new).
                collect(LinkedHashMap::new,
                        (m, v) -> m.computeIfAbsent(v.getDomain(), x -> new ArrayList<>()).add(v),
                        LinkedHashMap::putAll);
        });
    }

    public JmxBean getBeanDetails(Application app, String domain, String keyProperties) {
        return executeWithConnection(app, connection -> {
            try {
                ObjectName objectName = new ObjectName(domain + ":" + keyProperties);
                MBeanInfo beanInfo = connection.getMBeanInfo(objectName);

                List<JmxOperation> ops = Arrays.stream(beanInfo.getOperations()).
                    map(JmxOperation::new).
                    collect(Collectors.toList());

                List<JmxAttribute> attributes = Arrays.stream(beanInfo.getAttributes()).
                    map(JmxAttribute::new).
                    collect(Collectors.toList());

                return new JmxBean(objectName, beanInfo.getDescription(), ops, attributes);
            } catch (InstanceNotFoundException e) {
                return null;
            }
        });
    }

    public Map<String, Object> getBeanAttributeValues(Application app,
                                                      String domain,
                                                      String keyProperties) {
        return executeWithConnection(app, conn -> {
            ObjectName objectName = new ObjectName(domain + ":" + keyProperties);
            MBeanInfo mbeanInfo = conn.getMBeanInfo(objectName);

            String[] attributeNames = Arrays.stream(mbeanInfo.getAttributes()).
                filter(MBeanAttributeInfo::isReadable).
                map(MBeanAttributeInfo::getName).
                toArray(String[]::new);

            try {
                // try and read all of the attributes in one go
                return conn.getAttributes(objectName, attributeNames).asList().stream().
                    // can't use Collectors.toMap since we might have null values
                    collect(LinkedHashMap::new,
                            (m, v) -> m.put(v.getName(), v.getValue()),
                            LinkedHashMap::putAll);
            } catch (RemoteException e) {
                // sometimes we can't read an attribute (not on classpath, not serializable) so fall
                // back to reading attributes one at a time
                Map<String, Object> ret = new LinkedHashMap<>();
                for (String name : attributeNames) {
                    Object value;
                    try {
                        value = conn.getAttribute(objectName, name);
                    } catch (RemoteException err) {
                        value = Optional.of(err).
                            map(NestedExceptionUtils::getMostSpecificCause).
                            map(x -> "Error reading value: " + x).
                            get();
                    }
                    ret.put(name, value);
                }
                return ret;
            }
        });
    }

    public Object getBeanAttributeValue(Application app,
                                        String domain,
                                        String keyProperties,
                                        JmxAttribute attribute) {
        return executeWithConnection(app, conn -> {
            ObjectName objectName = new ObjectName(domain + ":" + keyProperties);
            return conn.getAttribute(objectName, attribute.getName());
        });
    }

    public void writeBeanAttributeValue(Application app,
                                        String domain,
                                        String keyProperties,
                                        JmxAttribute attribute,
                                        String value) {
        executeWithConnection(app, conn -> {
            // marshall value to the right type
            Class clazz = requireNonNull(CONVERTABLE_TYPES.get(attribute.getType()));
            Object marshalledValue = conversionService.convert(value, clazz);

            ObjectName objectName = new ObjectName(domain + ":" + keyProperties);
            conn.setAttribute(objectName, new Attribute(attribute.getName(), marshalledValue));
            return null;
        });
    }

    public Object executeOperation(Application app,
                                   String domain,
                                   String keyProperties,
                                   String operation,
                                   Object[] params,
                                   String[] signature) {
        return executeWithConnection(app, conn -> {
            if (params.length != signature.length) {
                throw new RuntimeException("Invalid params / signature");
            }

            // marshall params to the right type
            for (int i = 0; i < signature.length; i++) {
                Class clazz = requireNonNull(CONVERTABLE_TYPES.get(signature[i]));
                params[i] = conversionService.convert(params[i], clazz);
            }

            logExecution(app, domain, keyProperties, operation);

            ObjectName objectName = new ObjectName(domain + ":" + keyProperties);
            return conn.invoke(objectName, operation, params, signature);
        });
    }

    public boolean healthCheck(Application app) {
        return healthCheck(app, true);
    }

    private boolean healthCheck(Application app, boolean retry) {
        try {
            return executeWithConnection(app, c -> {
                c.getMBeanCount();
                return true;
            });
        } catch (Exception e) {
            // if the current connection we have for an app is invalid
            if (retry) {
                return healthCheck(app, false);
            }
            return false;
        }
    }

    @PreDestroy
    void cleanUpConnections() {
        connections.values().parallelStream().
            forEach(IOUtils::closeQuietly);
    }

    private void logExecution(Application app,
                              String domain,
                              String keyProperties,
                              String operation) {
        log.info("JMX Operation:\n\tUser: {}\n\tCluster: {}\n\tApp: {}\n\tMBean: {}:{}\n\tOperation: {}",
                 getUsername(),
                 app.getCluster().getName(),
                 app.getName(),
                 domain,
                 keyProperties,
                 operation);
    }

    private <R> R executeWithConnection(Application app, ConnectionFunction<R> f) {
        JMXConnector jmxConnector = connections.computeIfAbsent(app, this::createConnection);
        try {
            return f.doWithConnection(jmxConnector.getMBeanServerConnection());
        } catch (IOException e) {
            // close the connection; it might be in a bad state
            Optional.of(connections.remove(app)).
                ifPresent(IOUtils::closeQuietly);
            throw new RuntimeException(e);
        } catch (JMException e) {
            throw new RuntimeException(e);
        }
    }

    private JMXConnector createConnection(Application app) {
        try {
            // try ping-ing host first -- if host is unresponsive we might have to wait until jmx
            // timeout otherwise (which is measured in minutes)
            InetAddress addr = InetAddress.getByName(app.getHost());
            if (!addr.isReachable((int) PING_TIMEOUT.toMillis())) {
                throw new IOException("Host Not Reachable");
            }

            JMXServiceURL url = new JMXServiceURL(
                "service:jmx:rmi:///jndi/rmi://" + app.getHost() + ":" + app.getPort() + "/jmxrmi");
            HashMap<String, Object> env = new HashMap<>();

            if (app.getUsername() != null && app.getPassword() != null) {
                String[] credentials = {app.getUsername(), app.getPassword()};
                env.put(JMXConnector.CREDENTIALS, credentials);
            }

            return JMXConnectorFactory.connect(url, env);
        } catch (IOException e) {
            log.debug("Unable to create connection", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * We don't know what type we are going to get back from spring security for the current user,
     * so try and figure out the username as best we can.
     */
    private static String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof AuthenticatedPrincipal) {
            return ((AuthenticatedPrincipal) principal).getName();
        } else {
            return principal.toString();
        }
    }

    @FunctionalInterface
    private interface ConnectionFunction<R> {

        R doWithConnection(MBeanServerConnection connection) throws IOException, JMException;

    }
}
