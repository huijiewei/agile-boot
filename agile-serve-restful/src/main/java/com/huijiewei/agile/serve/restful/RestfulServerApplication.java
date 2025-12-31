package com.huijiewei.agile.serve.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author huijiewei
 */
@EnableCaching
@SpringBootApplication(exclude = {
        ErrorMvcAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class
})
@ComponentScan(basePackages = "com.huijiewei.agile")
@EntityScan(basePackages = "com.huijiewei.agile")
public class RestfulServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestfulServerApplication.class, args);
    }
}
