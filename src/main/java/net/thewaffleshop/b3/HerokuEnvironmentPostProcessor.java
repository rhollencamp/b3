package net.thewaffleshop.b3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;
import java.util.Map;

/**
 * Heroku doesn't appear to understand how docker is supposed to work, and wants the process inside the container to
 * bind to a port specified as an environment varaible.
 */
public class HerokuEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            Map<String, Object> map = Collections.singletonMap("server.port", herokuPort);
            environment.getPropertySources().addLast(new MapPropertySource("heroku", map));
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
