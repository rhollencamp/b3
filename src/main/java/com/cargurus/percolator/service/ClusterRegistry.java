package com.cargurus.percolator.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.cargurus.percolator.ClusterProperties;
import com.cargurus.percolator.domain.Application;
import com.cargurus.percolator.domain.Cluster;

@Component
public class ClusterRegistry {

    private final Object lockObj = new Object();
    private final LinkedHashMap<String, Cluster> clusters = new LinkedHashMap<>();

    private final ClusterProperties clusterProperties;

    public ClusterRegistry(ClusterProperties clusterProperties) {
        this.clusterProperties = clusterProperties;
    }

    @PostConstruct
    public void init() {
        synchronized (lockObj) {
            clusterProperties.getClusters().forEach(c -> {
                List<Application> apps = c.getApps().stream().
                    map(a -> new Application(a.getName(),
                                             a.getHost(),
                                             a.getPort(),
                                             a.getUser(),
                                             a.getPass())).
                    collect(Collectors.toList());

                Cluster cluster = new Cluster(c.getName(), apps);
                clusters.put(c.getName(), cluster);
            });
        }
    }

    public Cluster getCluster(String name) {
        synchronized (lockObj) {
            return clusters.get(name);
        }
    }

    public Application getApplication(String clusterName, String applicationName) {
        synchronized (lockObj) {
            return Optional.ofNullable(clusters.get(clusterName)).
                map(c -> c.getApplication(applicationName)).
                orElse(null);
        }
    }

    public Collection<Cluster> getClusters() {
        return clusters.values();
    }
}
