package com.huijiewei.agile.app.shop.adapter.persistence.repository;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopBrandCategory;
import com.huijiewei.agile.core.adapter.persistence.JpaBatchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author huijiewei
 */

@Repository
public interface JpaShopBrandCategoryRepository extends
        JpaBatchRepository<ShopBrandCategory>,
        JpaRepository<ShopBrandCategory, Integer> {
    @Modifying
    @Query("DELETE FROM ShopBrandCategory WHERE shopBrandId = ?1")
    void deleteAllByShopBrandId(Integer id);
}
