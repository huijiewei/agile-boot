package com.huijiewei.agile.app.cms.application.port.outbound;

import com.huijiewei.agile.app.cms.application.request.CmsArticleSearchRequest;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface CmsArticlePersistencePort {
    Boolean existsByCmsCategoryIds(List<Integer> cmsCategoryIds);

    SearchPageResponse<CmsArticleEntity> getAll(Integer page, Integer size, CmsArticleSearchRequest searchRequest, Boolean withSearchFields);

    Optional<CmsArticleEntity> getById(Integer id);

    Integer save(CmsArticleEntity cmsArticleEntity);

    void deleteById(Integer id);
}
