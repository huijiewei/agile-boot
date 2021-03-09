package com.huijiewei.agile.app.cms.application.service;

import com.huijiewei.agile.app.cms.application.mapper.CmsArticleRequestMapper;
import com.huijiewei.agile.app.cms.application.port.inbound.CmsArticleUseCase;
import com.huijiewei.agile.app.cms.application.port.inbound.CmsCategoryUseCase;
import com.huijiewei.agile.app.cms.application.port.outbound.CmsArticlePersistencePort;
import com.huijiewei.agile.app.cms.application.request.CmsArticleRequest;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;
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

    private CmsArticleEntity getById(Integer id) {
        return this.cmsArticlePersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("文章不存在"));
    }

    private CmsArticleEntity fillCategory(CmsArticleEntity cmsArticleEntity) {
        var cmsCategoryEntity = cmsArticleEntity.getCmsCategory();

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

        var cmsArticleEntity = this.cmsArticleRequestMapper.toCmsArticleEntity(cmsArticleRequest);

        if (!this.validatingService.validate(cmsArticleEntity)) {
            return null;
        }

        var articleId = this.cmsArticlePersistencePort.save(cmsArticleEntity);
        cmsArticleEntity.setId(articleId);

        return this.fillCategory(cmsArticleEntity);
    }

    @Override
    public CmsArticleEntity update(Integer id, CmsArticleRequest cmsArticleRequest) {
        var cmsArticleEntity = this.getById(id);

        if (!this.validatingService.validate(cmsArticleRequest)) {
            return null;
        }

        this.cmsArticleRequestMapper.updateCmsArticleEntity(cmsArticleRequest, cmsArticleEntity);

        if (!this.validatingService.validate(cmsArticleEntity)) {
            return null;
        }

        var articleId = this.cmsArticlePersistencePort.save(cmsArticleEntity);
        cmsArticleEntity.setId(articleId);

        return this.fillCategory(cmsArticleEntity);
    }

    @Override
    public void deleteById(Integer id) {
        var cmsArticleEntity = this.getById(id);

        this.cmsArticlePersistencePort.deleteById(cmsArticleEntity.getId());
    }
}
