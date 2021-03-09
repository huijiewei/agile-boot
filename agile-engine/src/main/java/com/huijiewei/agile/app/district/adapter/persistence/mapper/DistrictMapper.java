package com.huijiewei.agile.app.district.adapter.persistence.mapper;

import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.app.district.domain.DistrictEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author huijiewei
 */

@Mapper
public interface DistrictMapper {
    @Mapping(target = "parents", ignore = true)
    @Mapping(target = "children", ignore = true)
    DistrictEntity toDistrictEntity(District district);

    District toDistrict(DistrictEntity shopDistrictEntity);
}
