package com.huijiewei.agile.serve.admin.config;

import com.huijiewei.agile.serve.admin.security.AdminUserDetailsService;
import com.huijiewei.agile.serve.admin.security.PreAuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Collections;

/**
 * @author huijiewei
 */

@Configuration(proxyBeanMethods = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityProblemSupport.class)
public class WebSecurityConfig {
    private final SecurityProblemSupport problemSupport;
    private final AdminUserDetailsService adminUserDetailsService;

    public WebSecurityConfig(SecurityProblemSupport problemSupport, AdminUserDetailsService adminUserDetailsService) {
        this.problemSupport = problemSupport;
        this.adminUserDetailsService = adminUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(this.adminUserDetailsService);

        return new ProviderManager(Collections.singletonList(provider));
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        var provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(this.adminUserDetailsService);

        return provider;
    }

    @Bean
    public AbstractPreAuthenticatedProcessingFilter preAuthenticatedProcessingFilter() throws Exception {
        var filter = new PreAuthenticationTokenFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "**")
                .antMatchers("/favicon.ico", "/files/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.csrf().disable();
        http.cors();
        http.requestCache();
        http.logout().disable();
        http.formLogin().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling().authenticationEntryPoint(problemSupport).accessDeniedHandler(problemSupport);

        http.authorizeRequests().antMatchers("/auth/login", "/open/**").permitAll();

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(preAuthenticatedProcessingFilter());

        return http.build();
    }
}
