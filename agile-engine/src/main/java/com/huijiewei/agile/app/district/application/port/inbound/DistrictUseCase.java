package com.huijiewei.agile.app.district.application.port.inbound;

import com.huijiewei.agile.app.district.application.request.DistrictRequest;
import com.huijiewei.agile.app.district.domain.DistrictEntity;

import java.util.List;

/**
 * @author huijiewei
 */

public interface DistrictUseCase {
    List<DistrictEntity> getAllByParentId(Integer parentId);

    List<DistrictEntity> getPath(Integer id);

    List<DistrictEntity> getTreeByKeyword(String keyword);

    DistrictEntity read(Integer id, Boolean withParents);

    DistrictEntity create(DistrictRequest districtRequest);

    DistrictEntity update(Integer id, DistrictRequest districtRequest);

    void deleteById(Integer id);
}
