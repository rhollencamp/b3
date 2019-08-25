package com.cargurus.percolator.controllers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.cargurus.percolator.domain.Application;
import com.cargurus.percolator.domain.JmxBean;
import com.cargurus.percolator.domain.JmxOperation;
import com.cargurus.percolator.service.ClusterRegistry;
import com.cargurus.percolator.service.JmxService;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class OperationController extends BaseController {

    public OperationController(ClusterRegistry clusterRegistry, JmxService jmxService) {
        super(clusterRegistry, jmxService);
    }

    @GetMapping(path = "/operation")
    public ModelAndView viewOperation(@RequestParam("cluster") String clusterName,
                                      @RequestParam("app") String appName,
                                      @RequestParam("domain") String domain,
                                      @RequestParam("keyProperties") String keyProperties,
                                      @RequestParam("operation") String operationName,
                                      @RequestParam(name = "signature", defaultValue = "") List<String> signature) {
        ModelAndView mav = new ModelAndView("operation/view");
        mav.addObject("domain", domain);
        mav.addObject("keyProperties", keyProperties);

        Application app = getApplication(clusterName, appName);
        mav.addObject("app", app);

        JmxBean bean = getBeanDetails(app, domain, keyProperties);
        mav.addObject("bean", bean);

        JmxOperation operation = getOperation(bean, operationName, signature);
        mav.addObject("operation", operation);

        return mav;
    }

    @PostMapping(path = "/operation")
    public ModelAndView executeOperation(@RequestParam("cluster") String clusterName,
                                         @RequestParam("app") String appName,
                                         @RequestParam("domain") String domain,
                                         @RequestParam("keyProperties") String keyProperties,
                                         @RequestParam("operation") String operationName,
                                         @RequestParam("target") String target,
                                         @RequestParam(name = "signature", defaultValue = "") List<String> signature,
                                         @RequestParam(name = "argument", defaultValue = "") List<String> arguments) {
        ModelAndView mav = new ModelAndView("operation/execute");
        mav.addObject("domain", domain);
        mav.addObject("keyProperties", keyProperties);
        mav.addObject("target", target);

        Application app = getApplication(clusterName, appName);
        mav.addObject("app", app);

        JmxBean bean = getBeanDetails(app, domain, keyProperties);
        mav.addObject("bean", bean);

        JmxOperation operation = getOperation(bean, operationName, signature);
        mav.addObject("operation", operation);

        Collection<Application> targets = getTargets(app, target);

        Map<Application, String> result = new LinkedHashMap<>();
        for (Application targetApp : targets) {
            Object resultObj;
            try {
                resultObj = jmxService.executeOperation(targetApp,
                                                        domain,
                                                        keyProperties,
                                                        operationName,
                                                        arguments.toArray(),
                                                        signature.toArray(new String[0]));
            } catch (Exception e) {
                Throwable root = NestedExceptionUtils.getMostSpecificCause(e);
                if (root.getMessage() != null) {
                    resultObj = root.getClass().getSimpleName() + ": " + root.getMessage();
                } else {
                    resultObj = root.getClass().getSimpleName();
                }
            }
            result.put(targetApp, Objects.toString(resultObj));
        }
        mav.addObject("stdout", result);

        return mav;
    }

    private JmxOperation getOperation(JmxBean bean, String operationName, List<String> signature) {
        return Optional.ofNullable(bean.getOperation(operationName, signature)).
            orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

}
