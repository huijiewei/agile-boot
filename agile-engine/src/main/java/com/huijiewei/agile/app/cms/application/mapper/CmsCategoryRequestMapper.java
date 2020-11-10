package com.huijiewei.agile.app.cms.application.mapper;

import com.huijiewei.agile.app.cms.application.request.CmsCategoryRequest;
import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface CmsCategoryRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parents", ignore = true)
    CmsCategoryEntity toCmsCategoryEntity(CmsCategoryRequest cmsCategoryRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parents", ignore = true)
    void updateCmsCategoryEntity(CmsCategoryRequest cmsCategoryRequest, @MappingTarget CmsCategoryEntity cmsCategoryEntity);

}
