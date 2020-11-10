package com.huijiewei.agile.app.cms.application.service;

import com.huijiewei.agile.app.cms.application.mapper.CmsCategoryRequestMapper;
import com.huijiewei.agile.app.cms.application.port.inbound.CmsCategoryUseCase;
import com.huijiewei.agile.app.cms.application.port.outbound.CmsArticlePersistencePort;
import com.huijiewei.agile.app.cms.application.port.outbound.CmsCategoryPersistencePort;
import com.huijiewei.agile.app.cms.application.request.CmsCategoryRequest;
import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.ConflictException;
import com.huijiewei.agile.core.exception.NotFoundException;
import com.huijiewei.agile.core.until.TreeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class CmsCategoryService implements CmsCategoryUseCase {
    private final CmsCategoryPersistencePort cmsCategoryPersistencePort;
    private final CmsCategoryRequestMapper cmsCategoryRequestMapper;
    private final ValidatingService validatingService;
    private final CmsArticlePersistencePort cmsArticlePersistencePort;

    private List<CmsCategoryEntity> getParentsById(Integer id) {
        return TreeUtils.getParents(id, this.cmsCategoryPersistencePort.getAll());
    }

    @Override
    public List<CmsCategoryEntity> getTree() {
        return this.cmsCategoryPersistencePort.getTree();
    }

    @Override
    public List<CmsCategoryEntity> getPathById(Integer id) {
        return this.getParentsById(id);
    }

    private CmsCategoryEntity getById(Integer id) {
        return this.cmsCategoryPersistencePort.getById(id).orElseThrow(() -> new NotFoundException("商品分类不存在"));
    }

    @Override
    public CmsCategoryEntity read(Integer id, Boolean withParents) {
        CmsCategoryEntity cmsCategoryEntity = this.getById(id);

        if (withParents != null && withParents && cmsCategoryEntity.getParentId() > 0) {
            cmsCategoryEntity.setParents(this.getParentsById(cmsCategoryEntity.getParentId()));
        }

        return cmsCategoryEntity;
    }

    @Override
    public CmsCategoryEntity create(CmsCategoryRequest cmsCategoryRequest) {
        if (!this.validatingService.validate(cmsCategoryRequest)) {
            return null;
        }

        CmsCategoryEntity cmsCategoryEntity = this.cmsCategoryRequestMapper.toCmsCategoryEntity(cmsCategoryRequest);

        Integer shopCategoryId = this.cmsCategoryPersistencePort.save(cmsCategoryEntity);
        cmsCategoryEntity.setId(shopCategoryId);

        return cmsCategoryEntity;
    }

    @Override
    public CmsCategoryEntity update(Integer id, CmsCategoryRequest cmsCategoryRequest) {
        CmsCategoryEntity cmsCategoryEntity = this.getById(id);

        if (!this.validatingService.validate(cmsCategoryRequest)) {
            return null;
        }

        this.cmsCategoryRequestMapper.updateCmsCategoryEntity(cmsCategoryRequest, cmsCategoryEntity);

        if (!this.validatingService.validate(cmsCategoryEntity)) {
            return null;
        }

        Integer cmsCategoryId = this.cmsCategoryPersistencePort.save(cmsCategoryEntity);
        cmsCategoryEntity.setId(cmsCategoryId);

        cmsCategoryEntity.setParents(this.getParentsById(cmsCategoryEntity.getParentId()));

        return cmsCategoryEntity;
    }

    @Override
    public void deleteById(Integer id) {
        CmsCategoryEntity cmsCategoryEntity = this.getById(id);

        List<Integer> deleteIds = TreeUtils.getChildrenIds(cmsCategoryEntity.getId(), this.cmsCategoryPersistencePort.getTree());
        deleteIds.add(cmsCategoryEntity.getId());

        if (this.cmsArticlePersistencePort.existsByCmsCategoryIds(deleteIds)) {
            throw new ConflictException("内容分类内拥有文章，无法删除");
        }

        this.cmsCategoryPersistencePort.deleteAllById(deleteIds);
    }
}
