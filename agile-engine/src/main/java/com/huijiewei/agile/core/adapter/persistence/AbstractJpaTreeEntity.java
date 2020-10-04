package com.huijiewei.agile.core.adapter.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = false)
@Data
@MappedSuperclass
public abstract class AbstractJpaTreeEntity extends AbstractJpaEntity {
    private Integer parentId;
}
