package com.huijiewei.agile.core.adapter.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class AbstractJpaTreeClosureEntity extends AbstractJpaTreeEntity {
    protected abstract String getTableName();

    public String getClosureTableName() {
        return this.getTableName() + "_closure";
    }
}
