package com.huijiewei.agile.core.adapter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author huijiewei
 */

@Configuration(proxyBeanMethods = false)
public class FlywayConfiguration {
    @Bean
    public FlywayConfigurationCustomizer customize(@Value("${agile.database.table-prefix}") String tablePrefix) {
        return configuration -> configuration.placeholders(Map.of("table-prefix", tablePrefix));
    }
}
