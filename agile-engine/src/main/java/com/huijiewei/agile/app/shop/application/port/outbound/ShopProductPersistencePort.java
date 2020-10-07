package com.huijiewei.agile.app.shop.application.port.outbound;

import java.util.List;

/**
 * @author huijiewei
 */

public interface ShopProductPersistencePort {
    Boolean existsByShopBrandId(Integer shopBrandId);

    Boolean existsByShopCategoryIds(List<Integer> shopCategoryIds);
}
