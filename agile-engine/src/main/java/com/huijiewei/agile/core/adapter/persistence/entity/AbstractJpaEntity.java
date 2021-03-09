package com.huijiewei.agile.core.adapter.persistence.entity;

import com.huijiewei.agile.core.config.PrefixTableNamingStrategy;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author huijiewei
 */

@Data
@MappedSuperclass
public abstract class AbstractJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public static String tableName(Class<? extends AbstractJpaEntity> entityClass) {
        return PrefixTableNamingStrategy.toPhysicalTableName(entityClass.getSimpleName());
    }
}
