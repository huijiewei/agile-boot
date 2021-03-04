package com.huijiewei.agile.app.cms.application.port.outbound;

import com.huijiewei.agile.app.cms.application.request.CmsArticleSearchRequest;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

import java.util.Collection;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface CmsArticlePersistencePort {
    boolean existsByCmsCategoryIdIn(Collection<Integer> cmsCategoryIds);

    SearchPageResponse<CmsArticleEntity> search(CmsArticleSearchRequest searchRequest, PageRequest pageRequest, Boolean withSearchFields);

    Optional<CmsArticleEntity> getById(Integer id);

    int save(CmsArticleEntity cmsArticleEntity);

    void deleteById(Integer id);
}
