package com.huijiewei.agile.app.district.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class District extends AbstractJpaTreeEntity {
    private String name;

    private String code;

    private String zipCode;

    private String areaCode;
}
