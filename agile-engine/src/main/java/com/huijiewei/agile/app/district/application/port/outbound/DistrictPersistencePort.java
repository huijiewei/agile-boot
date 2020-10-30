package com.huijiewei.agile.app.district.application.port.outbound;

import com.huijiewei.agile.app.district.domain.DistrictEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface DistrictPersistencePort {
    Optional<DistrictEntity> getById(Integer id);

    Integer save(DistrictEntity districtEntity);

    void delete(DistrictEntity districtEntity);

    List<DistrictEntity> getAllByParentId(Integer parentId);

    List<DistrictEntity> getAncestorsById(Integer id);

    List<DistrictEntity> getAncestorsByKeyword(String keyword);

    List<DistrictEntity> getAncestorsTreeByKeyword(String keyword);

    Map<String, List<DistrictEntity>> getAllByCodesWithParents(List<String> code);
}
