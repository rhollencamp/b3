package com.cargurus.percolator.service;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.cargurus.percolator.domain.Application;
import com.cargurus.percolator.domain.Cluster;
import com.cargurus.percolator.domain.JmxAttribute;
import com.cargurus.percolator.domain.JmxOperation;
import com.cargurus.percolator.domain.JmxParameter;

@Component
public class UrlGenerator {

    public String cluster(Cluster cluster) {
        return UriComponentsBuilder.
            fromPath("/cluster").
            queryParam("cluster", cluster.getName()).
            toUriString();
    }

    public String app(Application application) {
        return UriComponentsBuilder.
            fromPath("/app").
            queryParam("cluster", application.getCluster().getName()).
            queryParam("app", application.getName()).
            toUriString();
    }

    public String domain(Application application,
                       String domain) {
        return UriComponentsBuilder.
            fromPath("/domain").
            queryParam("cluster", application.getCluster().getName()).
            queryParam("app", application.getName()).
            queryParam("domain", domain).
            toUriString();
    }

    public String bean(Application application,
                       String domain,
                       String keyProperties) {
        return UriComponentsBuilder.
            fromPath("/bean").
            queryParam("cluster", application.getCluster().getName()).
            queryParam("app", application.getName()).
            queryParam("domain", domain).
            queryParam("keyProperties", keyProperties).
            toUriString();
    }

    public String operation(Application application,
                            String domain,
                            String keyProperties,
                            JmxOperation operation) {
        Object[] signature = operation.getParameters().stream().
            map(JmxParameter::getType).toArray();

        return UriComponentsBuilder.
            fromPath("/operation").
            queryParam("cluster", application.getCluster().getName()).
            queryParam("app", application.getName()).
            queryParam("domain", domain).
            queryParam("keyProperties", keyProperties).
            queryParam("operation", operation.getName()).
            queryParam("signature", signature).
            toUriString();
    }

    public String attribute(Application application,
                            String domain,
                            String keyProperties,
                            JmxAttribute attribute) {
        return UriComponentsBuilder.
            fromPath("/attribute").
            queryParam("cluster", application.getCluster().getName()).
            queryParam("app", application.getName()).
            queryParam("domain", domain).
            queryParam("keyProperties", keyProperties).
            queryParam("attribute", attribute.getName()).
            toUriString();
    }
}
