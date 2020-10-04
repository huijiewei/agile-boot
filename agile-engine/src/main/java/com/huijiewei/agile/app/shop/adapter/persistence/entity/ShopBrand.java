package com.huijiewei.agile.app.shop.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DynamicInsert
@DynamicUpdate
public class ShopBrand extends AbstractJpaEntity {
    private String name;

    private String alias;

    private String logo;

    private String website;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ShopBrandCategory",
            joinColumns = @JoinColumn(name = "shopBrandId", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "shopCategoryId", insertable = false, updatable = false)
    )
    private List<ShopCategory> shopCategories = new ArrayList<>();
}
