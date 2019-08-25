package com.cargurus.percolator.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.web.server.ResponseStatusException;

import com.cargurus.percolator.domain.Application;
import com.cargurus.percolator.domain.Cluster;
import com.cargurus.percolator.domain.JmxAttribute;
import com.cargurus.percolator.domain.JmxBean;
import com.cargurus.percolator.service.ClusterRegistry;
import com.cargurus.percolator.service.JmxService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

abstract class BaseController {

    final ClusterRegistry clusterRegistry;
    final JmxService jmxService;

    BaseController(ClusterRegistry clusterRegistry, JmxService jmxService) {
        this.clusterRegistry = clusterRegistry;
        this.jmxService = jmxService;
    }

    Cluster getCluster(String clusterName) {
        return Optional.ofNullable(clusterName).
            map(clusterRegistry::getCluster).
            orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    Application getApplication(String clusterName, String appName) {
        return Optional.ofNullable(clusterName).
            map(clusterRegistry::getCluster).
            map(cluster -> cluster.getApplication(appName)).
            orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    JmxBean getBeanDetails(Application app, String domain, String keyProperties) {
        return Optional.ofNullable(jmxService.getBeanDetails(app, domain, keyProperties)).
            orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    JmxAttribute getBeanAttribute(JmxBean bean, String attributeName) {
        return Optional.ofNullable(bean.getAttribute(attributeName)).
            orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    Collection<Application> getTargets(Application app, String target) {
        if ("app".equals(target)) {
            return Collections.singleton(app);
        } else if ("cluster".equals(target)) {
            return app.getCluster().getApps();
        } else {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

}
