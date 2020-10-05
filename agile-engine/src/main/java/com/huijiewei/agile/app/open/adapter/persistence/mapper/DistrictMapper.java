package com.huijiewei.agile.app.open.adapter.persistence.mapper;

import com.huijiewei.agile.app.open.adapter.persistence.entity.District;
import com.huijiewei.agile.app.open.domain.DistrictEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DistrictMapper {
    @Mapping(target = "parents", ignore = true)
    @Mapping(target = "children", ignore = true)
    DistrictEntity toDistrictEntity(District district);

    District toDistrict(DistrictEntity shopDistrictEntity);
}
