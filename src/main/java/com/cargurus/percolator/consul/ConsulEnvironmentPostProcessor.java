package com.cargurus.percolator.consul;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

public class ConsulEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String consulUrl = environment.getProperty("percolator.consul.base-url");
        if (consulUrl != null) {
            Map<String, Object> src = getConsulClusterProperties(environment, new ConsulHelper(consulUrl));
            environment.getPropertySources().addLast(new MapPropertySource("consul", src));
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    Map<String, Object> getConsulClusterProperties(Environment env, ConsulHelper consulHelper) {
        Map<String, Object> ret = new HashMap<>();

        for (int clusterIndex = 0; hasDiscoveryNode(env, clusterIndex); clusterIndex++) {
            String clusterName = getDiscoveryProperty(env, "cluster-name", clusterIndex);
            putClusterConfig(ret, clusterIndex, "name", clusterName);

            String port = getDiscoveryProperty(env, "port", clusterIndex);
            String user = getDiscoveryProperty(env, "user", clusterIndex);
            String pass = getDiscoveryProperty(env, "pass", clusterIndex);

            String serviceName = getDiscoveryProperty(env, "service-name", clusterIndex);
            String tag = getDiscoveryProperty(env, "tags", clusterIndex);
            String[] tags = tag != null ? tag.split(",") : null;
            List<ServiceNode> serviceNodes = consulHelper.getServiceNodes(serviceName, tags);

            if (serviceNodes.isEmpty()) {
                throw new RuntimeException("No nodes found for consul cluster " + clusterName);
            }

            int appIndex = 0;
            for (ServiceNode node : serviceNodes) {
                putAppConfig(ret, clusterIndex, appIndex, "name", node.getNode());
                putAppConfig(ret, clusterIndex, appIndex, "host", node.getAddress());
                putAppConfig(ret, clusterIndex, appIndex, "port", port);

                if (user != null) {
                    putAppConfig(ret, clusterIndex, appIndex, "user", user);
                }
                if (pass != null) {
                    putAppConfig(ret, clusterIndex, appIndex, "pass", pass);
                }

                appIndex++;
            }
        }

        return ret;
    }

    private static void putClusterConfig(Map<String, Object> source,
                                         int clusterIndex,
                                         String prop,
                                         Object value) {
        String key = "percolator.clusters[" + clusterIndex + "]." + prop;
        source.put(key, value);
    }

    private static void putAppConfig(Map<String, Object> source,
                                     int clusterIndex,
                                     int appIndex,
                                     String prop,
                                     Object value) {
        String key = "percolator.clusters[" + clusterIndex + "].apps[" + appIndex + "]." + prop;
        source.put(key, value);
    }

    private static boolean hasDiscoveryNode(Environment env, int idx) {
        return getDiscoveryProperty(env, "service-name", idx) != null;
    }

    private static String getDiscoveryProperty(Environment env, String propName, int idx) {
        return env.getProperty("percolator.consul.discovery[" + idx + "]." + propName);
    }
}
