package com.huijiewei.agile.app.shop.adapter.persistence;

import com.huijiewei.agile.app.shop.adapter.persistence.mapper.ShopCategoryMapper;
import com.huijiewei.agile.app.shop.adapter.persistence.repository.ShopCategoryRepository;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@RequiredArgsConstructor
public class ShopCategoryCacheAdapter {
    public static final String CATEGORIES_CACHE_KEY = "shop-categories";
    public static final String CATEGORY_TREE_CACHE_KEY = "shop-category-tree";

    private final ShopCategoryRepository shopCategoryRepository;
    private final ShopCategoryMapper shopCategoryMapper;

    @Cacheable(cacheNames = CATEGORIES_CACHE_KEY)
    public List<ShopCategoryEntity> getAll() {
        return this.shopCategoryRepository
                .findAll()
                .stream()
                .map(this.shopCategoryMapper::toShopCategoryEntity)
                .collect(Collectors.toList());
    }
}
