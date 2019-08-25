package com.cargurus.percolator.consul;

import com.fasterxml.jackson.annotation.JsonProperty;

class ServiceNode {

    @JsonProperty("Node")           private String node;
    @JsonProperty("Address")        private String address;
    @JsonProperty("ServiceAddress") private String serviceAddress;
    @JsonProperty("ServicePort")    private Integer servicePort;

    String getNode() {
        return node;
    }

    void setNode(String node) {
        this.node = node;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    String getServiceAddress() {
        return serviceAddress;
    }

    void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    Integer getServicePort() {
        return servicePort;
    }

    void setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
    }
}
