package com.huijiewei.agile.app.cms.application.port.inbound;

import com.huijiewei.agile.app.cms.application.request.CmsCategoryRequest;
import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;

import java.util.List;

/**
 * @author huijiewei
 */

public interface CmsCategoryUseCase {
    List<CmsCategoryEntity> getTree();

    List<CmsCategoryEntity> getPathById(Integer id);

    CmsCategoryEntity read(Integer id, Boolean withParents);

    CmsCategoryEntity create(CmsCategoryRequest cmsCategoryRequest);

    CmsCategoryEntity update(Integer id, CmsCategoryRequest cmsCategoryRequest);

    void deleteById(Integer id);
}
