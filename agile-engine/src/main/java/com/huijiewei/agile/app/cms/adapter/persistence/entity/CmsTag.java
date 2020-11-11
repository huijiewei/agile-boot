package com.huijiewei.agile.app.cms.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaEntity;
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
public class CmsTag extends AbstractJpaEntity {
    private String name;

    private String icon;

    private String image;

    private String description;
}
