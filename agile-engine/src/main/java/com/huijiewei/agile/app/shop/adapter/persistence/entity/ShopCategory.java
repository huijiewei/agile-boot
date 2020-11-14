package com.huijiewei.agile.app.shop.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class ShopCategory extends AbstractJpaTreeEntity {
    private String name;

    private String icon;

    private String image;

    private String description;
}
