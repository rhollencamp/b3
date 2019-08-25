package com.cargurus.percolator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @ConditionalOnProperty("spring.security.oauth2.client.registration.ghe.client-id")
    @Bean
    WebSecurityConfigurerAdapter oauthSecurity() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.
                    antMatcher("/**").
                        authorizeRequests().
                        antMatchers("/login**", "/webjars/**", "/favicon.ico").permitAll().
                    anyRequest().authenticated().and().
                    logout().and().
                    oauth2Login().
                        loginPage("/login");
            }
        };
    }

    @ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
    @Bean
    WebSecurityConfigurerAdapter noSecurity() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().anyRequest().permitAll();
            }

            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.inMemoryAuthentication();
            }
        };
    }
}
