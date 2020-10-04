package com.huijiewei.agile.app.shop.application.mapper;

import com.huijiewei.agile.app.shop.application.request.ShopCategoryRequest;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShopCategoryRequestMapper {
    @Mapping(target = "children", ignore = true)
    ShopCategoryEntity toShopCategoryEntity(ShopCategoryRequest shopCategoryRequest);

    @Mapping(target = "children", ignore = true)
    ShopCategoryEntity toShopCategoryEntity(ShopCategoryRequest shopCategoryRequest, @MappingTarget ShopCategoryEntity shopCategoryEntity);

}
