package com.huijiewei.agile.app.shop.application.mapper;

import com.huijiewei.agile.app.shop.application.request.ShopBrandRequest;
import com.huijiewei.agile.app.shop.domain.ShopBrandEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author huijiewei
 */

@Mapper
public interface ShopBrandRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shopCategories", ignore = true)
    ShopBrandEntity toShopBrandEntity(ShopBrandRequest shopBrandRequest);

    @InheritConfiguration
    void updateShopBrandEntity(ShopBrandRequest shopBrandRequest, @MappingTarget ShopBrandEntity shopBrandEntity);

}
