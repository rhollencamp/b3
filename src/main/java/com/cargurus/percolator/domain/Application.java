package com.cargurus.percolator.domain;

import java.util.Objects;

public class Application {

    private final String name;
    private final String host;
    private final Integer port;
    private final String username;
    private final String password;
    private Cluster cluster;

    public Application(String name, String host, Integer port, String username, String password) {
        this.name = Objects.requireNonNull(name);
        this.host = Objects.requireNonNull(host);
        this.port = Objects.requireNonNull(port);
        this.username = username;
        this.password = password;
    }

    public Cluster getCluster() {
        return cluster;
    }

    void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Application that = (Application) o;
        return name.equals(that.name) && cluster.equals(that.cluster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cluster);
    }
}
