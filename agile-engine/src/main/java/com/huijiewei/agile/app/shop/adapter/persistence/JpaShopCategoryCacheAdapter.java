package com.huijiewei.agile.app.shop.adapter.persistence;

import com.huijiewei.agile.app.shop.adapter.persistence.mapper.ShopCategoryMapper;
import com.huijiewei.agile.app.shop.adapter.persistence.repository.JpaShopCategoryRepository;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
public class JpaShopCategoryCacheAdapter {
    public static final String SHOP_CATEGORIES_CACHE_KEY = "shop-categories";
    public static final String SHOP_CATEGORY_TREE_CACHE_KEY = "shop-category-tree";

    private final JpaShopCategoryRepository shopCategoryRepository;
    private final ShopCategoryMapper shopCategoryMapper;

    public JpaShopCategoryCacheAdapter(JpaShopCategoryRepository shopCategoryRepository, ShopCategoryMapper shopCategoryMapper) {
        this.shopCategoryRepository = shopCategoryRepository;
        this.shopCategoryMapper = shopCategoryMapper;
    }

    @Cacheable(cacheNames = SHOP_CATEGORIES_CACHE_KEY)
    public List<ShopCategoryEntity> getAll() {
        return this.shopCategoryRepository
                .findAll()
                .stream()
                .map(this.shopCategoryMapper::toShopCategoryEntity)
                .collect(Collectors.toList());
    }
}
