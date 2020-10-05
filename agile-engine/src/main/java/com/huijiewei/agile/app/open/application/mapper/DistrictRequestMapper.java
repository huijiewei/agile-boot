package com.huijiewei.agile.app.open.application.mapper;

import com.huijiewei.agile.app.open.application.request.DistrictRequest;
import com.huijiewei.agile.app.open.domain.DistrictEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface DistrictRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parents", ignore = true)
    DistrictEntity toDistrictEntity(DistrictRequest districtRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parents", ignore = true)
    void updateDistrictEntity(DistrictRequest districtRequest, @MappingTarget DistrictEntity districtEntity);

}
