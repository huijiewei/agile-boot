package com.huijiewei.agile.app.shop.application.service;

import com.huijiewei.agile.app.shop.application.port.inbound.ShopBrandUseCase;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopBrandPersistencePort;
import com.huijiewei.agile.app.shop.application.request.ShopBrandRequest;
import com.huijiewei.agile.app.shop.application.request.ShopBrandSearchRequest;
import com.huijiewei.agile.app.shop.domain.ShopBrandEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.exception.NotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author huijiewei
 */

@Service
public class ShopBrandService implements ShopBrandUseCase {
    private final ShopBrandPersistencePort shopBrandPersistencePort;

    public ShopBrandService(ShopBrandPersistencePort shopBrandPersistencePort) {
        this.shopBrandPersistencePort = shopBrandPersistencePort;
    }

    @Override
    public SearchPageResponse<ShopBrandEntity> all(Integer page, Integer size, ShopBrandSearchRequest searchRequest, Boolean withSearchFields) {
        return this.shopBrandPersistencePort.getAll(page, size, searchRequest, withSearchFields);
    }

    @Override
    public ShopBrandEntity read(Integer id) {
        return this.shopBrandPersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("商品品牌不存在"));
    }

    @Override
    public ShopBrandEntity create(ShopBrandRequest shopBrandRequest) {
        return null;
    }

    @Override
    public ShopBrandEntity update(Integer id, ShopBrandRequest shopBrandRequest) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
