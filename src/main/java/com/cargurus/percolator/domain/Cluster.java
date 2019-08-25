package com.cargurus.percolator.domain;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Cluster {

    private final String name;
    private final LinkedHashMap<String, Application> apps = new LinkedHashMap<>();

    public Cluster(String name, List<Application> apps) {
        this.name = Objects.requireNonNull(name);
        for (Application a : apps) {
            this.apps.put(a.getName(), a);
            a.setCluster(this);
        }
    }

    public String getName() {
        return name;
    }

    public Collection<Application> getApps() {
        return apps.values();
    }

    public Application getApplication(String applicationName) {
        return apps.get(applicationName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cluster cluster = (Cluster) o;
        return name.equals(cluster.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
