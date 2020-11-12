package com.huijiewei.agile.app.cms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.app.shop.application.service.ShopBrandUniqueService;
import com.huijiewei.agile.core.constraint.Unique;
import com.huijiewei.agile.core.domain.AbstractEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Unique(fields = {"slug"}, uniqueService = ShopBrandUniqueService.class, message = "文章别名已被占用")
public class CmsArticleEntity extends AbstractEntity {
    @Schema(description = "别名")
    private String slug;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "封面")
    private String image;

    @Schema(description = "介绍")
    private String description;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @JsonIgnore
    private Integer adminId;

    @Schema(description = "文章作者")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AdminEntity admin;

    @JsonIgnore
    private Integer cmsCategoryId;

    @Schema(description = "内容分类")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CmsCategoryEntity cmsCategory;

    @Schema(description = "文章标签")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CmsTagEntity> cmsTags = new ArrayList<>();
}
