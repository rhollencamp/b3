package com.cargurus.percolator.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.cargurus.percolator.domain.Application;
import com.cargurus.percolator.domain.Cluster;
import com.cargurus.percolator.domain.JmxBean;
import com.cargurus.percolator.service.ClusterRegistry;
import com.cargurus.percolator.service.JmxService;

import static java.util.stream.Collectors.toSet;

@Controller
public class BrowseController extends BaseController {

    public BrowseController(ClusterRegistry clusterRegistry, JmxService jmxService) {
        super(clusterRegistry, jmxService);
    }

    @GetMapping(path = "/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        Collection<Cluster> clusters = clusterRegistry.getClusters();
        mav.addObject("clusters", clusters);

        Set<Application> healthyNodes = clusters.parallelStream().
            flatMap(c -> c.getApps().stream()).
            filter(jmxService::healthCheck).
            collect(toSet());
        mav.addObject("healthyNodes", healthyNodes);

        return mav;
    }

    @GetMapping(path = "/cluster")
    public ModelAndView viewCluster(@RequestParam("cluster") String clusterName) {
        ModelAndView mav = new ModelAndView("index");

        Cluster cluster = getCluster(clusterName);
        mav.addObject("clusters", Collections.singleton(cluster));

        Set<Application> healthyNodes = cluster.getApps().parallelStream().
            filter(jmxService::healthCheck).
            collect(toSet());
        mav.addObject("healthyNodes", healthyNodes);

        return mav;
    }

    @GetMapping(path = "/app")
    public ModelAndView viewApp(@RequestParam("cluster") String clusterName,
                                @RequestParam("app") String appName) {
        ModelAndView mav = new ModelAndView("app");

        Application app = getApplication(clusterName, appName);
        mav.addObject("app", app);

        Map<String, List<JmxBean>> beans = jmxService.listBeans(app);
        mav.addObject("beans", beans);

        return mav;
    }

    @GetMapping(path = "/domain")
    public ModelAndView viewDomain(@RequestParam("cluster") String clusterName,
                                   @RequestParam("app") String appName,
                                   @RequestParam("domain") String domain) {
        ModelAndView mav = new ModelAndView("app");

        Application app = getApplication(clusterName, appName);
        mav.addObject("app", app);

        Collection<JmxBean> beans = jmxService.listBeans(app, domain).get(domain);
        mav.addObject("beans", Collections.singletonMap(domain, beans));

        return mav;
    }

    @GetMapping(path = "/bean")
    public ModelAndView viewBean(@RequestParam("cluster") String clusterName,
                                 @RequestParam("app") String appName,
                                 @RequestParam("domain") String domain,
                                 @RequestParam("keyProperties") String keyProperties) {
        ModelAndView mav = new ModelAndView("bean");
        mav.addObject("domain", domain);
        mav.addObject("keyProperties", keyProperties);

        Application app = getApplication(clusterName, appName);
        mav.addObject("app", app);

        JmxBean bean = jmxService.getBeanDetails(app, domain, keyProperties);
        if (bean == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        mav.addObject("bean", bean);

        Map<String, Object> attributes = jmxService.getBeanAttributeValues(app,
                                                                           domain,
                                                                           keyProperties).
            entrySet().
            stream().
            collect(TreeMap::new,
                    (m, a) -> m.put(a.getKey(), Objects.toString(a.getValue())),
                    TreeMap::putAll);
        mav.addObject("attributes", attributes);

        return mav;
    }
}
