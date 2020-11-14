package com.huijiewei.agile.app.shop.adapter.persistence.repository;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author huijiewei
 */

@Repository
public interface ShopCategoryRepository extends
        JpaRepository<ShopCategory, Integer>,
        JpaSpecificationExecutor<ShopCategory> {
    void deleteByIdIn(Collection<Integer> ids);
}
