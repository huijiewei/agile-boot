package com.huijiewei.agile.app.open.application.port.outbound;

import com.huijiewei.agile.app.open.domain.DistrictEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface DistrictPersistencePort {
    Optional<DistrictEntity> getById(Integer id);

    Integer save(DistrictEntity districtEntity);

    void deleteById(Integer id);

    List<DistrictEntity> getAllByParentId(Integer parentId);

    List<DistrictEntity> getParentByIdWithParents(Integer id);

    List<DistrictEntity> getTreeByKeyword(String keyword);
}
