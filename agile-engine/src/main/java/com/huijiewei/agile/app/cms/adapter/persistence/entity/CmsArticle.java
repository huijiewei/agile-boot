package com.huijiewei.agile.app.cms.adapter.persistence.entity;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.Admin;
import com.huijiewei.agile.core.adapter.persistence.AbstractJpaEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private String image;

    private String description;

    private String content;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Integer cmsCategoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cmsCategoryId", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private CmsCategory cmsCategory;

    private Integer adminId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adminId", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Admin admin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CmsArticleTag",
            joinColumns = @JoinColumn(name = "cmsArticleId", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "cmsTagId", insertable = false, updatable = false)
    )
    private List<CmsTag> cmsTags = new ArrayList<>();
}
