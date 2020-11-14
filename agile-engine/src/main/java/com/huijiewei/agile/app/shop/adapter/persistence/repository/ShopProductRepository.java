package com.huijiewei.agile.app.shop.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopProduct;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author huijiewei
 */

@Repository
public interface ShopProductRepository extends
        EntityGraphJpaRepository<ShopProduct, Integer>,
        EntityGraphJpaSpecificationExecutor<ShopProduct> {
    Boolean existsByShopBrandId(Integer shopBrandId);

    Boolean existsByShopCategoryIdIn(Collection<Integer> shopCategoryIds);
}
