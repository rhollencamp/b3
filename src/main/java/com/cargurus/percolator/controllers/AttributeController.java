package com.cargurus.percolator.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cargurus.percolator.domain.Application;
import com.cargurus.percolator.domain.JmxAttribute;
import com.cargurus.percolator.domain.JmxBean;
import com.cargurus.percolator.service.ClusterRegistry;
import com.cargurus.percolator.service.JmxService;

@Controller
public class AttributeController extends BaseController {

    public AttributeController(ClusterRegistry clusterRegistry, JmxService jmxService) {
        super(clusterRegistry, jmxService);
    }

    @GetMapping(path = "/attribute",
                params = {"cluster", "app", "domain", "keyProperties", "attribute"})
    public ModelAndView viewAttribute(@RequestParam("cluster") String clusterName,
                                      @RequestParam("app") String appName,
                                      @RequestParam("domain") String domain,
                                      @RequestParam("keyProperties") String keyProperties,
                                      @RequestParam("attribute") String attributeName) {
        ModelAndView mav = new ModelAndView("attribute/view");
        mav.addObject("domain", domain);
        mav.addObject("keyProperties", keyProperties);

        Application app = getApplication(clusterName, appName);
        mav.addObject("app", app);

        JmxBean bean = getBeanDetails(app, domain, keyProperties);
        mav.addObject("bean", bean);

        JmxAttribute attribute = getBeanAttribute(bean, attributeName);
        mav.addObject("attribute", attribute);

        return mav;
    }

    @GetMapping(path = "/attribute",
                params = {"cluster", "app", "domain", "keyProperties", "attribute", "target"})
    public ModelAndView readAttribute(@RequestParam("cluster") String clusterName,
                                      @RequestParam("app") String appName,
                                      @RequestParam("domain") String domain,
                                      @RequestParam("keyProperties") String keyProperties,
                                      @RequestParam("attribute") String attributeName,
                                      @RequestParam("target") String target) {
        ModelAndView mav = new ModelAndView("attribute/read");
        mav.addObject("domain", domain);
        mav.addObject("keyProperties", keyProperties);
        mav.addObject("target", target);

        Application app = getApplication(clusterName, appName);
        mav.addObject("app", app);

        JmxBean bean = getBeanDetails(app, domain, keyProperties);
        mav.addObject("bean", bean);

        JmxAttribute attribute = getBeanAttribute(bean, attributeName);
        mav.addObject("attribute", attribute);

        Collection<Application> targets = getTargets(app, target);
        HashMap<Application, String> attributeValues = new LinkedHashMap<>();
        for (Application targetApp : targets) {
            Object value;
            try {
                value = jmxService.getBeanAttributeValue(targetApp,
                                                         domain,
                                                         keyProperties,
                                                         attribute);
            } catch (Exception e) {
                Throwable root = NestedExceptionUtils.getMostSpecificCause(e);
                value = root.getClass().getSimpleName();
                if (root.getMessage() != null) {
                    value += ": " + root.getMessage();
                }
            }
            attributeValues.put(targetApp, Objects.toString(value));
        }
        mav.addObject("attributeValues", attributeValues);

        return mav;
    }

    @PostMapping(path = "/attribute")
    public ModelAndView write(@RequestParam("cluster") String clusterName,
                              @RequestParam("app") String appName,
                              @RequestParam("domain") String domain,
                              @RequestParam("keyProperties") String keyProperties,
                              @RequestParam("attribute") String attributeName,
                              @RequestParam("value") String value,
                              @RequestParam("target") String target) {
        ModelAndView mav = new ModelAndView("attribute/write");
        mav.addObject("domain", domain);
        mav.addObject("keyProperties", keyProperties);
        mav.addObject("target", target);

        Application app = getApplication(clusterName, appName);
        mav.addObject("app", app);

        JmxBean bean = getBeanDetails(app, domain, keyProperties);
        mav.addObject("bean", bean);

        JmxAttribute attribute = getBeanAttribute(bean, attributeName);
        mav.addObject("attribute", attribute);

        Collection<Application> targets = getTargets(app, target);
        HashMap<Application, String> results = new LinkedHashMap<>();
        for (Application targetApp : targets) {
            String result;
            try {
                jmxService.writeBeanAttributeValue(targetApp,
                                                   domain,
                                                   keyProperties,
                                                   attribute,
                                                   value);
                result = "null";
            } catch (Exception e) {
                Throwable root = NestedExceptionUtils.getMostSpecificCause(e);
                result = root.getClass().getSimpleName();
                if (root.getMessage() != null) {
                    result += ": " + root.getMessage();
                }
            }
            results.put(targetApp, result);
        }
        mav.addObject("results", results);

        return mav;
    }
}
