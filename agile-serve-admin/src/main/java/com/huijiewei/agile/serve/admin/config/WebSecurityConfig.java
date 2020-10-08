package com.huijiewei.agile.serve.admin.config;

import com.huijiewei.agile.serve.admin.security.AdminPreAuthenticationFilter;
import com.huijiewei.agile.serve.admin.security.AdminUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

/**
 * @author huijiewei
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityProblemSupport.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SecurityProblemSupport problemSupport;
    private final AdminUserDetailsService adminUserDetailsService;

    public WebSecurityConfig(SecurityProblemSupport problemSupport, AdminUserDetailsService adminUserDetailsService) {
        this.problemSupport = problemSupport;
        this.adminUserDetailsService = adminUserDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(this.adminUserDetailsService);

        builder.authenticationProvider(provider);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "**");
        web.ignoring().antMatchers("/favicon.ico", "/files/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/auth/login",
                        "/open/**",
                        "/druid/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .cors()
                .and()
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .sessionManagement().disable()
                .exceptionHandling().authenticationEntryPoint(problemSupport).accessDeniedHandler(problemSupport)
                .and()
                .addFilter(new AdminPreAuthenticationFilter(this.authenticationManager()));
    }
}
