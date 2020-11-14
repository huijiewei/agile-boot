package com.huijiewei.agile.app.shop.application.mapper;

import com.huijiewei.agile.app.shop.application.request.ShopCategoryRequest;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface ShopCategoryRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parents", ignore = true)
    ShopCategoryEntity toShopCategoryEntity(ShopCategoryRequest shopCategoryRequest);

    @InheritConfiguration
    void updateShopCategoryEntity(ShopCategoryRequest shopCategoryRequest, @MappingTarget ShopCategoryEntity shopCategoryEntity);

}
