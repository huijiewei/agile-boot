package com.huijiewei.agile.app.shop.adapter.persistence;

import com.huijiewei.agile.app.shop.adapter.persistence.mapper.ShopCategoryMapper;
import com.huijiewei.agile.app.shop.adapter.persistence.repository.ShopCategoryRepository;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryExistsPort;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryPersistencePort;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
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
public class ShopCategoryAdapter implements ShopCategoryExistsPort, ShopCategoryPersistencePort {
    private final ShopCategoryRepository shopCategoryRepository;
    private final ShopCategoryMapper shopCategoryMapper;
    private final ShopCategoryCacheAdapter shopCategoryCacheAdapter;

    @Override
    public boolean exists(String targetProperty, List<String> values) {
        return this.shopCategoryRepository.count(JpaSpecificationBuilder.buildExists(targetProperty, values)) > 0;
    }

    @Override
    public List<ShopCategoryEntity> getAll() {
        return this.shopCategoryCacheAdapter.getAll();
    }

    @Override
    @Cacheable(cacheNames = ShopCategoryCacheAdapter.CATEGORY_TREE_CACHE_KEY)
    public List<ShopCategoryEntity> getTree() {
        return TreeUtils.buildTree(this.getAll());
    }

    @Override
    public Optional<ShopCategoryEntity> getById(Integer id) {
        return this.shopCategoryRepository.findById(id).map(this.shopCategoryMapper::toShopCategoryEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {ShopCategoryCacheAdapter.CATEGORIES_CACHE_KEY, ShopCategoryCacheAdapter.CATEGORY_TREE_CACHE_KEY}, allEntries = true)
    public Integer save(ShopCategoryEntity shopCategoryEntity) {
        var shopCategory = this.shopCategoryRepository.save(this.shopCategoryMapper.toShopCategory(shopCategoryEntity));

        return shopCategory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {ShopCategoryCacheAdapter.CATEGORIES_CACHE_KEY, ShopCategoryCacheAdapter.CATEGORY_TREE_CACHE_KEY}, allEntries = true)
    public void deleteAllById(List<Integer> ids) {
        this.shopCategoryRepository.deleteByIdIn(ids);
    }
}
