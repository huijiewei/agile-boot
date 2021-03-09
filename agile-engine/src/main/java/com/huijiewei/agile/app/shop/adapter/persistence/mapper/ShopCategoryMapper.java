package com.huijiewei.agile.app.shop.adapter.persistence.mapper;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopCategory;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author huijiewei
 */

@Mapper
public interface ShopCategoryMapper {
    @Mapping(target = "parents", ignore = true)
    @Mapping(target = "children", ignore = true)
    ShopCategoryEntity toShopCategoryEntity(ShopCategory shopCategory);

    ShopCategory toShopCategory(ShopCategoryEntity shopCategoryEntity);
}
