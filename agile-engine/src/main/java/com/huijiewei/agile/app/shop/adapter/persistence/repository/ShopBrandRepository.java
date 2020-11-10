package com.huijiewei.agile.app.shop.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopBrand;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author huijiewei
 */

@Repository
public interface ShopBrandRepository extends
        EntityGraphJpaRepository<ShopBrand, Integer>,
        EntityGraphJpaSpecificationExecutor<ShopBrand> {
    Optional<ShopBrand> findBySlug(String slug);
}
