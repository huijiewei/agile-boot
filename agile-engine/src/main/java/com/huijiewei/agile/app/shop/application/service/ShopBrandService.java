package com.huijiewei.agile.app.shop.application.service;

import com.huijiewei.agile.app.shop.application.mapper.ShopBrandRequestMapper;
import com.huijiewei.agile.app.shop.application.port.inbound.ShopBrandUseCase;
import com.huijiewei.agile.app.shop.application.port.inbound.ShopCategoryUseCase;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopBrandPersistencePort;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopProductPersistencePort;
import com.huijiewei.agile.app.shop.application.request.ShopBrandRequest;
import com.huijiewei.agile.app.shop.application.request.ShopBrandSearchRequest;
import com.huijiewei.agile.app.shop.domain.ShopBrandEntity;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.ConflictException;
import com.huijiewei.agile.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class ShopBrandService implements ShopBrandUseCase {
    private final ShopBrandPersistencePort shopBrandPersistencePort;
    private final ShopCategoryUseCase shopCategoryUseCase;
    private final ValidatingService validatingService;
    private final ShopBrandRequestMapper shopBrandRequestMapper;
    private final ShopProductPersistencePort shopProductPersistencePort;

    @Override
    public SearchPageResponse<ShopBrandEntity> search(ShopBrandSearchRequest searchRequest, PageRequest pageRequest, Boolean withSearchFields) {
        return this.shopBrandPersistencePort.getAll(searchRequest, pageRequest, withSearchFields);
    }

    private ShopBrandEntity getById(Integer id) {
        return this.shopBrandPersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("商品品牌不存在"));
    }

    @Override
    public ShopBrandEntity loadById(Integer id) {
        return this.fillCategories(this.getById(id));
    }

    private ShopBrandEntity fillCategories(ShopBrandEntity shopBrandEntity) {
        List<ShopCategoryEntity> shopCategoryResponses = shopBrandEntity.getShopCategories();

        for (int i = 0; i < shopCategoryResponses.size(); i++) {
            ShopCategoryEntity shopCategoryEntity = shopCategoryResponses.get(i);
            shopCategoryEntity.setParents(this.shopCategoryUseCase.loadPathById(shopCategoryEntity.getParentId()));
            shopCategoryResponses.set(i, shopCategoryEntity);
        }

        shopBrandEntity.setShopCategories(shopCategoryResponses);

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
        ShopBrandEntity shopBrandEntity = this.getById(id);

        if (!this.validatingService.validate(shopBrandRequest)) {
            return null;
        }

        this.shopBrandRequestMapper.updateShopBrandEntity(shopBrandRequest, shopBrandEntity);

        if (!this.validatingService.validate(shopBrandEntity)) {
            return null;
        }

        Integer shopBrandId = this.shopBrandPersistencePort.save(shopBrandEntity);
        shopBrandEntity.setId(shopBrandId);

        return this.fillCategories(shopBrandEntity);
    }

    @Override
    public void deleteById(Integer id) {
        ShopBrandEntity shopBrandEntity = this.getById(id);

        if (this.shopProductPersistencePort.existsByShopBrandId(shopBrandEntity.getId())) {
            throw new ConflictException("商品品牌内拥有商品，无法删除");
        }

        this.shopBrandPersistencePort.deleteById(shopBrandEntity.getId());
    }
}
