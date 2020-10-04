package com.huijiewei.agile.app.shop.application.service;

import com.huijiewei.agile.app.shop.application.mapper.ShopBrandRequestMapper;
import com.huijiewei.agile.app.shop.application.port.inbound.ShopBrandUseCase;
import com.huijiewei.agile.app.shop.application.port.inbound.ShopCategoryUseCase;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopBrandPersistencePort;
import com.huijiewei.agile.app.shop.application.request.ShopBrandRequest;
import com.huijiewei.agile.app.shop.application.request.ShopBrandSearchRequest;
import com.huijiewei.agile.app.shop.domain.ShopBrandEntity;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
public class ShopBrandService implements ShopBrandUseCase {
    private final ShopBrandPersistencePort shopBrandPersistencePort;
    private final ShopCategoryUseCase shopCategoryUseCase;
    private final ValidatingService validatingService;
    private final ShopBrandRequestMapper shopBrandRequestMapper;

    public ShopBrandService(ShopBrandPersistencePort shopBrandPersistencePort, ShopCategoryUseCase shopCategoryUseCase, ValidatingService validatingService, ShopBrandRequestMapper shopBrandRequestMapper) {
        this.shopBrandPersistencePort = shopBrandPersistencePort;
        this.shopCategoryUseCase = shopCategoryUseCase;
        this.validatingService = validatingService;
        this.shopBrandRequestMapper = shopBrandRequestMapper;
    }

    @Override
    public SearchPageResponse<ShopBrandEntity> all(Integer page, Integer size, ShopBrandSearchRequest searchRequest, Boolean withSearchFields) {
        return this.shopBrandPersistencePort.getAll(page, size, searchRequest, withSearchFields);
    }

    private ShopBrandEntity getById(Integer id) {
        return this.shopBrandPersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("商品品牌不存在"));
    }

    @Override
    public ShopBrandEntity read(Integer id) {
        return this.fillCategories(this.getById(id));
    }

    private ShopBrandEntity fillCategories(ShopBrandEntity shopBrandEntity) {
        List<ShopCategoryEntity> shopCategoryResponses = shopBrandEntity.getShopCategories();

        if (shopCategoryResponses != null) {
            for (int i = 0; i < shopCategoryResponses.size(); i++) {
                ShopCategoryEntity shopCategoryEntity = shopCategoryResponses.get(i);
                shopCategoryEntity.setParents(this.shopCategoryUseCase.getPath(shopCategoryEntity.getParentId()));
                shopCategoryResponses.set(i, shopCategoryEntity);
            }

            shopBrandEntity.setShopCategories(shopCategoryResponses);
        }

        return shopBrandEntity;
    }

    @Override
    public ShopBrandEntity create(ShopBrandRequest shopBrandRequest) {
        if (!this.validatingService.validate(shopBrandRequest)) {
            return null;
        }

        ShopBrandEntity shopBrandEntity = this.shopBrandRequestMapper.toShopBrandEntity(shopBrandRequest);

        if (!this.validatingService.validate(shopBrandEntity)) {
            return null;
        }

        Integer shopBrandId = this.shopBrandPersistencePort.save(shopBrandEntity);
        shopBrandEntity.setId(shopBrandId);

        return this.fillCategories(shopBrandEntity);
    }

    @Override
    public ShopBrandEntity update(Integer id, ShopBrandRequest shopBrandRequest) {
        ShopBrandEntity currentShopBrandEntity = this.getById(id);

        if (!this.validatingService.validate(shopBrandRequest)) {
            return null;
        }

        ShopBrandEntity shopBrandEntity = this.shopBrandRequestMapper.toShopBrandEntity(shopBrandRequest, currentShopBrandEntity);

        if (!this.validatingService.validate(shopBrandEntity)) {
            return null;
        }

        Integer shopBrandId = this.shopBrandPersistencePort.save(shopBrandEntity);
        shopBrandEntity.setId(shopBrandId);

        return this.fillCategories(shopBrandEntity);
    }

    @Override
    public void deleteById(Integer id) {
        ShopBrandEntity currentShopBrandEntity = this.getById(id);

        this.shopBrandPersistencePort.deleteById(currentShopBrandEntity.getId());
    }
}
