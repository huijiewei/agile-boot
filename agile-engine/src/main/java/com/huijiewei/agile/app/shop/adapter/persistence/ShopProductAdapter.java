package com.huijiewei.agile.app.shop.adapter.persistence;

import com.huijiewei.agile.app.shop.adapter.persistence.repository.ShopProductRepository;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopProductAdapter implements ShopProductPersistencePort {
    private final ShopProductRepository shopProductRepository;

    @Override
    public Boolean existsByShopBrandId(Integer shopBrandId) {
        return this.shopProductRepository.existsByShopBrandId(shopBrandId);
    }

    @Override
    public Boolean existsByShopCategoryIds(List<Integer> shopCategoryIds) {
        return this.shopProductRepository.existsByShopCategoryIds(shopCategoryIds);
    }
}
