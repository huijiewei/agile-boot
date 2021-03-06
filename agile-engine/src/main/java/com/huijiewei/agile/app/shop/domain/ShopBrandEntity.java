package com.huijiewei.agile.app.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.huijiewei.agile.app.shop.application.service.ShopBrandUniqueService;
import com.huijiewei.agile.core.constraint.Unique;
import com.huijiewei.agile.core.domain.AbstractEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Unique.List({
        @Unique(fields = {"name"}, uniqueService = ShopBrandUniqueService.class, message = "品牌已存在"),
        @Unique(fields = {"slug"}, uniqueService = ShopBrandUniqueService.class, message = "品牌别名已被占用")
})
public class ShopBrandEntity extends AbstractEntity {
    @Schema(description = "名称")
    private String name;

    @Schema(description = "别名")
    private String slug;

    @Schema(description = "LOGO")
    private String logo;

    @Schema(description = "网站")
    private String website;

    @Schema(description = "介绍")
    private String description;

    @JsonIgnore
    private List<Integer> shopCategoryIds = new ArrayList<>();

    @Schema(description = "商品分类")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ShopCategoryEntity> shopCategories = new ArrayList<>();
}
