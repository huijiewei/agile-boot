package com.huijiewei.agile.app.district.application.port.inbound;

import com.huijiewei.agile.app.district.application.request.DistrictRequest;
import com.huijiewei.agile.app.district.domain.DistrictEntity;

import java.util.List;

/**
 * @author huijiewei
 */

public interface DistrictUseCase {
    List<DistrictEntity> loadByParentId(Integer parentId);

    List<DistrictEntity> loadPathById(Integer id);

    List<DistrictEntity> loadTreeByKeyword(String keyword);

    DistrictEntity loadById(Integer id, Boolean withParents);

    DistrictEntity create(DistrictRequest districtRequest);

    DistrictEntity update(Integer id, DistrictRequest districtRequest);

    void deleteById(Integer id);
}
