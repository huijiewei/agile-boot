package com.huijiewei.agile.core.adapter.config;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author huijiewei
 */

@Getter
@Setter
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = PrefixTableNamingStrategy.PREFIX)
@ConditionalOnProperty(prefix = PrefixTableNamingStrategy.PREFIX, name = "table-prefix")
public class PrefixTableNamingStrategy implements PhysicalNamingStrategy {
    public static final String PREFIX = "agile.database";

    public static String tablePrefix;

    public static String toPhysicalTableName(final String entityName) {
        final var regex = "([a-z])([A-Z])";
        final var replacement = "$1_$2";
        return tablePrefix + entityName
                .replaceAll(regex, replacement)
                .toLowerCase();
    }

    @Value("${agile.database.table-prefix}")
    public void setDatabaseTablePrefix(String tablePrefix) {
        PrefixTableNamingStrategy.tablePrefix = tablePrefix;
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return name;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return name;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return Identifier.toIdentifier(toPhysicalTableName(name.getText()), true);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return name;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return name;
    }
}
