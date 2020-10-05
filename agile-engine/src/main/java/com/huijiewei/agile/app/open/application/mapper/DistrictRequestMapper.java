package com.huijiewei.agile.app.open.application.mapper;

import com.huijiewei.agile.app.open.application.request.DistrictRequest;
import com.huijiewei.agile.app.open.domain.DistrictEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DistrictRequestMapper {
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parents", ignore = true)
    DistrictEntity toDistrictEntity(DistrictRequest districtRequest);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parents", ignore = true)
    DistrictEntity toDistrictEntity(DistrictRequest districtRequest, @MappingTarget DistrictEntity districtEntity);

}
