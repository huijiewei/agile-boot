package com.huijiewei.agile.app.shop.adapter.persistence.repository;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopBrandCategory;
import com.huijiewei.agile.core.adapter.persistence.repository.BatchJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huijiewei
 */

@Repository
public interface ShopBrandCategoryRepository extends
        BatchJpaRepository<ShopBrandCategory>,
        JpaRepository<ShopBrandCategory, Integer> {
    void deleteByShopBrandId(Integer shopBrandId);
}
