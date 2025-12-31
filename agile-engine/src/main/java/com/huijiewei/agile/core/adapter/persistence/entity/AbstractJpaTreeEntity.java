package com.huijiewei.agile.core.adapter.persistence.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = false)
@Data
@MappedSuperclass
public abstract class AbstractJpaTreeEntity extends AbstractJpaEntity {
    private Integer parentId;
}
