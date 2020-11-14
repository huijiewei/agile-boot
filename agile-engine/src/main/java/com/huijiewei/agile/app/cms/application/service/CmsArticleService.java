package com.huijiewei.agile.app.cms.application.service;

import com.huijiewei.agile.app.cms.application.mapper.CmsArticleRequestMapper;
import com.huijiewei.agile.app.cms.application.port.inbound.CmsArticleUseCase;
import com.huijiewei.agile.app.cms.application.port.inbound.CmsCategoryUseCase;
import com.huijiewei.agile.app.cms.application.port.outbound.CmsArticlePersistencePort;
import com.huijiewei.agile.app.cms.application.request.CmsArticleRequest;
import com.huijiewei.agile.app.cms.application.request.CmsArticleSearchRequest;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;
import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class CmsArticleService implements CmsArticleUseCase {
    private final CmsArticlePersistencePort cmsArticlePersistencePort;
    private final CmsCategoryUseCase cmsCategoryUseCase;
    private final ValidatingService validatingService;
    private final CmsArticleRequestMapper cmsArticleRequestMapper;

    @Override
    public SearchPageResponse<CmsArticleEntity> search(CmsArticleSearchRequest searchRequest, Integer page, Integer size, Boolean withSearchFields) {
        return this.cmsArticlePersistencePort.getAll(page, size, searchRequest, withSearchFields);
    }

    private CmsArticleEntity getById(Integer id) {
        return this.cmsArticlePersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("文章不存在"));
    }

    private CmsArticleEntity fillCategory(CmsArticleEntity cmsArticleEntity) {
        CmsCategoryEntity cmsCategoryEntity = cmsArticleEntity.getCmsCategory();

        if (cmsCategoryEntity != null) {
            cmsCategoryEntity.setParents(this.cmsCategoryUseCase.loadPathById(cmsCategoryEntity.getParentId()));

            cmsArticleEntity.setCmsCategory(cmsCategoryEntity);
        }

        return cmsArticleEntity;
    }

    @Override
    public CmsArticleEntity loadById(Integer id) {
        return this.fillCategory(this.getById(id));
    }

    @Override
    public CmsArticleEntity create(CmsArticleRequest cmsArticleRequest) {
        if (!this.validatingService.validate(cmsArticleRequest)) {
            return null;
        }

        CmsArticleEntity cmsArticleEntity = this.cmsArticleRequestMapper.toCmsArticleEntity(cmsArticleRequest);

        if (!this.validatingService.validate(cmsArticleEntity)) {
            return null;
        }

        Integer articleId = this.cmsArticlePersistencePort.save(cmsArticleEntity);
        cmsArticleEntity.setId(articleId);

        return this.fillCategory(cmsArticleEntity);
    }

    @Override
    public CmsArticleEntity update(Integer id, CmsArticleRequest cmsArticleRequest) {
        CmsArticleEntity cmsArticleEntity = this.getById(id);

        if (!this.validatingService.validate(cmsArticleRequest)) {
            return null;
        }

        this.cmsArticleRequestMapper.updateCmsArticleEntity(cmsArticleRequest, cmsArticleEntity);

        if (!this.validatingService.validate(cmsArticleEntity)) {
            return null;
        }

        Integer articleId = this.cmsArticlePersistencePort.save(cmsArticleEntity);
        cmsArticleEntity.setId(articleId);

        return this.fillCategory(cmsArticleEntity);
    }

    @Override
    public void deleteById(Integer id) {
        CmsArticleEntity cmsArticleEntity = this.getById(id);

        this.cmsArticlePersistencePort.deleteById(cmsArticleEntity.getId());
    }
}
