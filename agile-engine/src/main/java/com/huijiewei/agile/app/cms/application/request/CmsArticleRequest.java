package com.huijiewei.agile.app.cms.application.request;

import com.huijiewei.agile.app.cms.application.service.CmsCategoryExistsService;
import com.huijiewei.agile.core.constraint.Exists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author huijiewei
 */
@Data
public class CmsArticleRequest {
    @NotBlank
    @Schema(description = "文章别名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String slug;

    @NotBlank
    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank
    @Schema(description = "文章图片", requiredMode = Schema.RequiredMode.REQUIRED)
    private String image;

    @NotBlank
    @Schema(description = "文章简介", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @NotBlank
    @Schema(description = "文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    private Integer adminId;

    @NotNull
    @Schema(description = "内容分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @Exists(existService = CmsCategoryExistsService.class, targetProperty = "id", allowValues = "0", message = "内容分类不存在")
    private Integer cmsCategoryId;

    @Schema(description = "关联分类")
    private List<String> cmsTags;
}
