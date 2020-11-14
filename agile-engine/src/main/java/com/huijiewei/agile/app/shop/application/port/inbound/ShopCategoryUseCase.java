package com.huijiewei.agile.app.shop.application.port.inbound;

import com.huijiewei.agile.app.shop.application.request.ShopCategoryRequest;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;

import java.util.List;

/**
 * @author huijiewei
 */

public interface ShopCategoryUseCase {
    List<ShopCategoryEntity> loadTree();

    List<ShopCategoryEntity> loadPathById(Integer id);

    ShopCategoryEntity loadById(Integer id, Boolean withParents);

    ShopCategoryEntity create(ShopCategoryRequest shopCategoryRequest);

    ShopCategoryEntity update(Integer id, ShopCategoryRequest shopCategoryRequest);

    void deleteById(Integer id);
}
