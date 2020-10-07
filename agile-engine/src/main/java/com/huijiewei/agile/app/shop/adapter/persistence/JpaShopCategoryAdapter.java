package com.huijiewei.agile.app.shop.adapter.persistence;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopCategory;
import com.huijiewei.agile.app.shop.adapter.persistence.mapper.ShopCategoryMapper;
import com.huijiewei.agile.app.shop.adapter.persistence.repository.JpaShopCategoryRepository;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryExistsPort;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryPersistencePort;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import com.huijiewei.agile.core.adapter.persistence.ExistsSpecificationBuilder;
import com.huijiewei.agile.core.until.TreeUtils;
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
public class JpaShopCategoryAdapter implements ShopCategoryExistsPort, ShopCategoryPersistencePort {

    private final JpaShopCategoryRepository shopCategoryRepository;
    private final ShopCategoryMapper shopCategoryMapper;
    private final JpaShopCategoryCacheAdapter jpaShopCategoryCacheAdapter;

    public JpaShopCategoryAdapter(JpaShopCategoryRepository shopCategoryRepository, ShopCategoryMapper shopCategoryMapper, JpaShopCategoryCacheAdapter jpaShopCategoryCacheAdapter) {
        this.shopCategoryRepository = shopCategoryRepository;
        this.shopCategoryMapper = shopCategoryMapper;
        this.jpaShopCategoryCacheAdapter = jpaShopCategoryCacheAdapter;
    }

    @Override
    public Boolean exists(String targetProperty, List<String> values) {
        return this.shopCategoryRepository.count(ExistsSpecificationBuilder.build(targetProperty, values)) > 0;
    }

    @Override
    public List<ShopCategoryEntity> getAll() {
        return this.jpaShopCategoryCacheAdapter.getAll();
    }

    @Override
    @Cacheable(cacheNames = JpaShopCategoryCacheAdapter.SHOP_CATEGORY_TREE_CACHE_KEY)
    public List<ShopCategoryEntity> getTree() {
        return TreeUtils.buildTree(this.getAll());
    }

    @Override
    public Optional<ShopCategoryEntity> getById(Integer id) {
        return this.shopCategoryRepository.findById(id).map(this.shopCategoryMapper::toShopCategoryEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {JpaShopCategoryCacheAdapter.SHOP_CATEGORIES_CACHE_KEY, JpaShopCategoryCacheAdapter.SHOP_CATEGORY_TREE_CACHE_KEY}, allEntries = true)
    public Integer save(ShopCategoryEntity shopCategoryEntity) {
        ShopCategory shopCategory = this.shopCategoryRepository.save(this.shopCategoryMapper.toShopCategory(shopCategoryEntity));

        return shopCategory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {JpaShopCategoryCacheAdapter.SHOP_CATEGORIES_CACHE_KEY, JpaShopCategoryCacheAdapter.SHOP_CATEGORY_TREE_CACHE_KEY}, allEntries = true)
    public void deleteAllById(List<Integer> ids) {
        this.shopCategoryRepository.deleteAllById(ids);
    }
}
