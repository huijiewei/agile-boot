package com.huijiewei.agile.serve.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

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
}
