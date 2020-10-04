package com.huijiewei.agile.app.shop.application.mapper;

import com.huijiewei.agile.app.shop.application.request.ShopBrandRequest;
import com.huijiewei.agile.app.shop.domain.ShopBrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShopBrandRequestMapper {
    @Mapping(target = "shopCategories", ignore = true)
    ShopBrandEntity toShopBrandEntity(ShopBrandRequest shopBrandRequest);

    @Mapping(target = "shopCategories", ignore = true)
    ShopBrandEntity toShopBrandEntity(ShopBrandRequest shopBrandRequest, @MappingTarget ShopBrandEntity shopBrandEntity);

}
