package com.huijiewei.agile.app.cms.application.port.outbound;

import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface CmsCategoryPersistencePort {
    List<CmsCategoryEntity> getAll();

    List<CmsCategoryEntity> getTree();

    Optional<CmsCategoryEntity> getById(Integer id);

    Integer save(CmsCategoryEntity cmsCategoryEntity);

    void deleteAllById(List<Integer> ids);
}
