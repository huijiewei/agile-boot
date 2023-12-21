package com.huijiewei.agile.core.adapter.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.MappedSuperclass;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = false)
@Data
@MappedSuperclass
public abstract class AbstractJpaTreeEntity extends AbstractJpaEntity {
    private Integer parentId;
}
