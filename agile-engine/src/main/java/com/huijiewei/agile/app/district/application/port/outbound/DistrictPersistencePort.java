package com.huijiewei.agile.app.district.application.port.outbound;

import com.huijiewei.agile.app.district.domain.DistrictEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface DistrictPersistencePort {
    Optional<DistrictEntity> getById(Integer id);

    Integer save(DistrictEntity districtEntity);

    void deleteAllById(List<Integer> ids);

    List<DistrictEntity> getChildrenById(Integer id);

    List<DistrictEntity> getAllByParentId(Integer parentId);

    List<DistrictEntity> getParentsById(Integer id);

    List<DistrictEntity> getTreeByKeyword(String keyword);
}
