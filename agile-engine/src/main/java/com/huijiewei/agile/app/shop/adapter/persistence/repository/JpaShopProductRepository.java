package com.huijiewei.agile.app.shop.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huijiewei
 */

@Repository
public interface JpaShopProductRepository extends
        EntityGraphJpaRepository<ShopProduct, Integer>,
        EntityGraphJpaSpecificationExecutor<ShopProduct> {
    Boolean existsByShopBrandId(Integer shopBrandId);

    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END FROM ShopProduct WHERE shopCategoryId IN ?1")
    Boolean existsByShopCategoryIds(List<Integer> shopCategoryIds);
}
