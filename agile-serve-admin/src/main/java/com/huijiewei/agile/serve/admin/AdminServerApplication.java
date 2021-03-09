package com.huijiewei.agile.serve.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
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
                UserDetailsServiceAutoConfiguration.class
        })
@EntityScan(basePackages = "com.huijiewei.agile")
public class AdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }
}
