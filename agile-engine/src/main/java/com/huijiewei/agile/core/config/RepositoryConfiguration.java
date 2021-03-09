package com.huijiewei.agile.core.config;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

/**
 * @author huijiewei
 */

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(
        repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class,
        basePackages = "com.huijiewei.agile",
        bootstrapMode = BootstrapMode.DEFERRED
)
public class RepositoryConfiguration {
}
