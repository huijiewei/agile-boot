package com.huijiewei.agile.app.cms.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author huijiewei
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class CmsArticle extends AbstractJpaEntity {
    private Integer cmsCategoryId;

    private String title;

    @Column(updatable = false)
    private LocalDateTime createdAt;
}
