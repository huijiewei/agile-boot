package com.huijiewei.agile.app.district.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaTreeClosureEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class District extends AbstractJpaTreeClosureEntity {
    private String name;

    private String code;

    private String zipCode;

    private String areaCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", insertable = false, updatable = false)
    private District parent;

    @Override
    protected String getTableName() {
        return District.tableName(District.class);
    }
}
