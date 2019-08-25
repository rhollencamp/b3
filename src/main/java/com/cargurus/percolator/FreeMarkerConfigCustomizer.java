package com.cargurus.percolator;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.cargurus.percolator.domain.Breadcrumb;
import com.cargurus.percolator.service.UrlGenerator;
import freemarker.template.Configuration;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

@Component
public class FreeMarkerConfigCustomizer {

    private final Configuration config;
    private final UrlGenerator urlGenerator;

    public FreeMarkerConfigCustomizer(Configuration config, UrlGenerator urlGenerator) {
        this.config = config;
        this.urlGenerator = urlGenerator;
    }

    @PostConstruct
    public void customizeFreemarker() throws TemplateModelException {
        config.setSharedVariable("urlGenerator", urlGenerator);
        config.setSharedVariable("breadcrumb", new BreadcrumbInstantiator());
        config.setSharedVariable("getPrincipal", new SecurityPrincipal());
    }

    private static class BreadcrumbInstantiator implements TemplateMethodModelEx {

        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.size() < 1 || arguments.size() > 2) {
                throw new TemplateModelException("Invalid arguments");
            }

            String title = arguments.get(0).toString();
            String url = null;
            if (arguments.size() > 1) {
                url = arguments.get(1).toString();
            }
            return new Breadcrumb(title, url);
        }
    }

    private static class SecurityPrincipal implements  TemplateMethodModelEx {

        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (!arguments.isEmpty()) {
                throw new TemplateModelException("Invalid arguments");
            }
            return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
    }
}
