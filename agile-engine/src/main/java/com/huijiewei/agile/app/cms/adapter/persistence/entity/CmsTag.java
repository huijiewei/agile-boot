package com.huijiewei.agile.app.cms.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class CmsTag extends AbstractJpaEntity {
    private String name;

    private String icon;

    private String image;

    private String description;
}
