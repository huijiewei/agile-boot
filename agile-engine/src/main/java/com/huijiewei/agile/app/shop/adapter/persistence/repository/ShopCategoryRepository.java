package com.huijiewei.agile.app.shop.adapter.persistence.repository;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huijiewei
 */

@Repository
public interface ShopCategoryRepository extends
        JpaRepository<ShopCategory, Integer>,
        JpaSpecificationExecutor<ShopCategory> {
    @Modifying
    @Query("DELETE FROM ShopCategory WHERE id IN ?1")
    void deleteAllById(List<Integer> ids);
}
