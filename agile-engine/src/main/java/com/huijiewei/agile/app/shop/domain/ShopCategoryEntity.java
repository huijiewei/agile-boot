package com.huijiewei.agile.app.shop.domain;

import com.huijiewei.agile.core.domain.AbstractTreeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ShopCategoryEntity extends AbstractTreeEntity<ShopCategoryEntity> {
    @Schema(description = "名称")
    private String name;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "图片")
    private String image;

    @Schema(description = "介绍")
    private String description;
}
