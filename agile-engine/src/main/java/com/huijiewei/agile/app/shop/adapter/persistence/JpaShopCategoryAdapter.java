package com.huijiewei.agile.app.shop.adapter.persistence;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopCategory;
import com.huijiewei.agile.app.shop.adapter.persistence.mapper.ShopCategoryMapper;
import com.huijiewei.agile.app.shop.adapter.persistence.repository.JpaShopCategoryRepository;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryExistsPort;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryPersistencePort;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import com.huijiewei.agile.core.until.TreeUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JpaShopCategoryAdapter implements ShopCategoryExistsPort, ShopCategoryPersistencePort {
    public static final String SHOP_CATEGORIES_CACHE_KEY = "shop-categories";
    private static final String SHOP_CATEGORY_TREE_CACHE_KEY = "shop-category-tree";

    private final JpaShopCategoryRepository shopCategoryRepository;
    private final ShopCategoryMapper shopCategoryMapper;

    public JpaShopCategoryAdapter(JpaShopCategoryRepository shopCategoryRepository, ShopCategoryMapper shopCategoryMapper) {
        this.shopCategoryRepository = shopCategoryRepository;
        this.shopCategoryMapper = shopCategoryMapper;
    }

    @Override
    public Boolean exist(String targetProperty, List<String> values) {
        Specification<ShopCategory> adminGroupSpecification = (Specification<ShopCategory>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            for (String value : values) {
                predicates.add(criteriaBuilder.equal(root.get(targetProperty), value));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return this.shopCategoryRepository.count(adminGroupSpecification) > 0;
    }

    @Override
    @Cacheable(cacheNames = SHOP_CATEGORIES_CACHE_KEY)
    public List<ShopCategoryEntity> getAll() {
        return this.shopCategoryRepository
                .findAll()
                .stream()
                .map(this.shopCategoryMapper::toShopCategoryEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = SHOP_CATEGORY_TREE_CACHE_KEY)
    public List<ShopCategoryEntity> getTree() {
        return TreeUtils.buildTree(this.getAll());
    }

    @Override
    public Optional<ShopCategoryEntity> getById(Integer id) {
        return this.shopCategoryRepository.findById(id).map(this.shopCategoryMapper::toShopCategoryEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {SHOP_CATEGORIES_CACHE_KEY, SHOP_CATEGORY_TREE_CACHE_KEY}, allEntries = true)
    public Integer save(ShopCategoryEntity shopCategoryEntity) {
        ShopCategory shopCategory = this.shopCategoryRepository.save(this.shopCategoryMapper.toShopCategory(shopCategoryEntity));

        return shopCategory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {SHOP_CATEGORIES_CACHE_KEY, SHOP_CATEGORY_TREE_CACHE_KEY}, allEntries = true)
    public void deleteById(Integer id) {
        this.shopCategoryRepository.deleteAllByIds(TreeUtils.getChildrenIds(id, true, this.getTree()));
    }
}
