package com.huijiewei.agile.app.shop.adapter.persistence;

import com.huijiewei.agile.app.shop.adapter.persistence.repository.JpaShopProductRepository;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopProductPersistencePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
public class JpaShopProductAdapter implements ShopProductPersistencePort {
    private final JpaShopProductRepository shopProductRepository;

    public JpaShopProductAdapter(JpaShopProductRepository shopProductRepository) {
        this.shopProductRepository = shopProductRepository;
    }

    @Override
    public Boolean existsByShopBrandId(Integer shopBrandId) {
        return this.shopProductRepository.existsByShopBrandId(shopBrandId);
    }

    @Override
    public Boolean existsByShopCategoryIds(List<Integer> shopCategoryIds) {
        return this.shopProductRepository.existsByShopCategoryIds(shopCategoryIds);
    }
}
