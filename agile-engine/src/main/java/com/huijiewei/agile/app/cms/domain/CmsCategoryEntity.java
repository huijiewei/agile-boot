package com.huijiewei.agile.app.cms.domain;

import com.huijiewei.agile.app.shop.application.service.ShopBrandUniqueService;
import com.huijiewei.agile.core.constraint.Unique;
import com.huijiewei.agile.core.domain.AbstractTreeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huijiewei
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Unique.List({
        @Unique(fields = {"name"}, uniqueService = ShopBrandUniqueService.class, message = "分类已存在"),
        @Unique(fields = {"slug"}, uniqueService = ShopBrandUniqueService.class, message = "分类别名已被占用")
})
public class CmsCategoryEntity extends AbstractTreeEntity<CmsCategoryEntity> {
    @Schema(description = "名称")
    private String name;

    @Schema(description = "别名")
    private String slug;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "图片")
    private String image;

    @Schema(description = "介绍")
    private String description;
}
