package com.huijiewei.agile.app.shop.application.port.outbound;

import com.huijiewei.agile.app.shop.application.request.ShopBrandSearchRequest;
import com.huijiewei.agile.app.shop.domain.ShopBrandEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

import java.util.Optional;

/**
 * @author huijiewei
 */

public interface ShopBrandPersistencePort {
    SearchPageResponse<ShopBrandEntity> getAll(Integer page, Integer size, ShopBrandSearchRequest searchRequest, Boolean withSearchFields);

    Optional<ShopBrandEntity> getById(Integer id);

    Integer save(ShopBrandEntity shopBrandEntity);

    void deleteById(Integer id);
}
