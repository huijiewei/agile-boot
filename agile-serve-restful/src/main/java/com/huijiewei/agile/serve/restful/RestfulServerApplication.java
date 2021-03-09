package com.huijiewei.agile.serve.restful;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

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
