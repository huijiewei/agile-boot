package com.huijiewei.agile.app.shop.application.port.outbound;

import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface ShopCategoryPersistencePort {
    List<ShopCategoryEntity> getAll();

    List<ShopCategoryEntity> getTree();

    Optional<ShopCategoryEntity> getById(Integer id);

    Integer save(ShopCategoryEntity shopCategoryEntity);

    void deleteAllById(List<Integer> ids);
}
