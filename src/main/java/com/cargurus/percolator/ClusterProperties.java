package com.cargurus.percolator;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "percolator")
@Component
@Validated
public class ClusterProperties {

    @NotEmpty private List<Cluster> clusters;

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    @Validated
    public static class App {

        @NotEmpty private String name;
        @NotEmpty private String host;
        @NotNull @Positive private Integer port;
        private String user;
        private String pass;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }
    }

    @Validated
    public static class Cluster {

        @NotEmpty private String name;
        @NotEmpty private List<App> apps;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<App> getApps() {
            return apps;
        }

        public void setApps(List<App> apps) {
            this.apps = apps;
        }
    }
}
