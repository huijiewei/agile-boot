package com.huijiewei.agile.app.shop.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Entity;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class ShopBrandCategory extends AbstractJpaEntity {
    private Integer shopBrandId;

    private Integer shopCategoryId;
}
