package com.huijiewei.agile.app.cms.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * @author huijiewei
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class CmsArticleTag extends AbstractJpaEntity {
    private Integer cmsTagId;

    private Integer cmsArticleId;
}
