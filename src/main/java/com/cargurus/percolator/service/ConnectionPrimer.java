package com.cargurus.percolator.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Initialize JMX connections for all of the apps in the registry
 */
@Component
class ConnectionPrimer {

    private final Logger log = LoggerFactory.getLogger(ConnectionPrimer.class);

    private final ClusterRegistry clusterRegistry;
    private final JmxService jmxService;

    public ConnectionPrimer(ClusterRegistry clusterRegistry, JmxService jmxService) {
        this.clusterRegistry = clusterRegistry;
        this.jmxService = jmxService;
    }

    @PostConstruct
    public void primeConnections() {
        log.info("Initializing connections");
        clusterRegistry.getClusters().parallelStream().
            flatMap(c -> c.getApps().stream()).
            forEach(jmxService::healthCheck);
    }
}
