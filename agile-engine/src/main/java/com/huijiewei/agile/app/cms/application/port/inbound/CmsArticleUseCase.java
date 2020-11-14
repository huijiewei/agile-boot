package com.huijiewei.agile.app.cms.application.port.inbound;

import com.huijiewei.agile.app.cms.application.request.CmsArticleRequest;
import com.huijiewei.agile.app.cms.application.request.CmsArticleSearchRequest;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

/**
 * @author huijiewei
 */

public interface CmsArticleUseCase {
    CmsArticleEntity loadById(Integer id);

    CmsArticleEntity create(CmsArticleRequest cmsArticleRequest);

    CmsArticleEntity update(Integer id, CmsArticleRequest cmsArticleRequest);

    void deleteById(Integer id);
}
