package com.huijiewei.agile.app.cms.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huijiewei
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class CmsArticle extends AbstractJpaEntity {
    private String slug;

    private String title;

    private String cover;

    private String description;

    private String content;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Integer cmsCategoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cmsCategoryId", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private CmsCategory cmsCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CmsArticleTag",
            joinColumns = @JoinColumn(name = "cmsArticleId", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "cmsTagId", insertable = false, updatable = false)
    )
    private List<CmsTag> cmsTags;
}
