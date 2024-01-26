package com.huijiewei.agile.serve.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;

/**
 * @author huijiewei
 */
@EnableCaching
@SpringBootApplication(
        proxyBeanMethods = false,
        scanBasePackages = "com.huijiewei.agile",
        exclude = {
                ErrorMvcAutoConfiguration.class,
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        })
@EntityScan(basePackages = "com.huijiewei.agile")
public class AdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }

    @Bean
    public UndertowDeploymentInfoCustomizer undertowDeploymentInfoCustomizer() {
        return deploymentInfo -> deploymentInfo.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
