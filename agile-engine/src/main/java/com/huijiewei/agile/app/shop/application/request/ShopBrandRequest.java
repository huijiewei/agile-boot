package com.huijiewei.agile.app.shop.application.request;

import com.huijiewei.agile.app.shop.application.service.ShopCategoryExistsService;
import com.huijiewei.agile.core.constraint.Exists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author huijiewei
 */

@Data
public class ShopBrandRequest {
    @NotBlank
    @Schema(description = "品牌名称", required = true)
    private String name;

    @NotBlank
    @Schema(description = "品牌别名", required = true)
    private String slug;

    @NotNull
    @Schema(description = "品牌 LOGO")
    private String logo;

    @Schema(description = "品牌网站")
    private String website;

    @Schema(description = "品牌介绍")
    private String description;

    @Exists(existService = ShopCategoryExistsService.class, targetProperty = "id", message = "选择了无效的关联分类")
    @Schema(description = "关联分类")
    private List<Integer> shopCategoryIds;
}
