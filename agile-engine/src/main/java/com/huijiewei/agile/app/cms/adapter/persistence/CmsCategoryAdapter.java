package com.huijiewei.agile.app.cms.adapter.persistence;

import com.huijiewei.agile.app.cms.adapter.persistence.mapper.CmsCategoryMapper;
import com.huijiewei.agile.app.cms.adapter.persistence.repository.CmsCategoryRepository;
import com.huijiewei.agile.app.cms.application.port.outbound.CmsCategoryExistsPort;
import com.huijiewei.agile.app.cms.application.port.outbound.CmsCategoryPersistencePort;
import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;
import com.huijiewei.agile.core.adapter.persistence.JpaSpecificationBuilder;
import com.huijiewei.agile.core.until.TreeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CmsCategoryAdapter implements CmsCategoryExistsPort, CmsCategoryPersistencePort {
    private final CmsCategoryRepository cmsCategoryRepository;
    private final CmsCategoryMapper cmsCategoryMapper;
    private final CmsCategoryCacheAdapter cmsCategoryCacheAdapter;

    @Override
    public boolean exists(String targetProperty, List<String> values) {
        return this.cmsCategoryRepository.count(JpaSpecificationBuilder.buildExists(targetProperty, values)) > 0;
    }

    @Override
    public List<CmsCategoryEntity> getAll() {
        return this.cmsCategoryCacheAdapter.getAll();
    }

    @Override
    @Cacheable(cacheNames = CmsCategoryCacheAdapter.CATEGORY_TREE_CACHE_KEY)
    public List<CmsCategoryEntity> getTree() {
        return TreeUtils.buildTree(this.getAll());
    }

    @Override
    public Optional<CmsCategoryEntity> getById(Integer id) {
        return this.cmsCategoryRepository.findById(id).map(this.cmsCategoryMapper::toCmsCategoryEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CmsCategoryCacheAdapter.CATEGORIES_CACHE_KEY, CmsCategoryCacheAdapter.CATEGORY_TREE_CACHE_KEY}, allEntries = true)
    public Integer save(CmsCategoryEntity cmsCategoryEntity) {
        var cmsCategory = this.cmsCategoryRepository.save(this.cmsCategoryMapper.toCmsCategory(cmsCategoryEntity));

        return cmsCategory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {CmsCategoryCacheAdapter.CATEGORIES_CACHE_KEY, CmsCategoryCacheAdapter.CATEGORY_TREE_CACHE_KEY}, allEntries = true)
    public void deleteAllById(List<Integer> ids) {
        this.cmsCategoryRepository.deleteByIdIn(ids);
    }
}
