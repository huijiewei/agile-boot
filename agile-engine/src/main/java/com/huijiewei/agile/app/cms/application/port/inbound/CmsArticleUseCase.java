package com.huijiewei.agile.app.cms.application.port.inbound;

import com.huijiewei.agile.app.cms.application.request.CmsArticleRequest;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;

/**
 * @author huijiewei
 */

public interface CmsArticleUseCase {
    CmsArticleEntity loadById(Integer id);

    CmsArticleEntity create(CmsArticleRequest cmsArticleRequest);

    CmsArticleEntity update(Integer id, CmsArticleRequest cmsArticleRequest);

    void deleteById(Integer id);
}
