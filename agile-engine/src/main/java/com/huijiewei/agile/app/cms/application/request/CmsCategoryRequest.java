package com.huijiewei.agile.app.cms.application.request;

import com.huijiewei.agile.app.cms.application.service.CmsCategoryExistsService;
import com.huijiewei.agile.core.constraint.Exists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author huijiewei
 */

@Data
public class CmsCategoryRequest {
    @NotNull
    @Schema(description = "上级分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @Exists(existService = CmsCategoryExistsService.class, targetProperty = "id", allowValues = "0", message = "内容分类不存在")
    private Integer parentId;

    @NotBlank
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description = "分类别名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String slug;

    @NotNull
    @Schema(description = "分类图标")
    private String icon;

    @NotNull
    @Schema(description = "分类图片")
    private String image;

    @Schema(description = "分类介绍")
    private String description;
}
