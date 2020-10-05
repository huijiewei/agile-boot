package com.huijiewei.agile.app.shop.application.service;

import com.huijiewei.agile.app.shop.application.mapper.ShopCategoryRequestMapper;
import com.huijiewei.agile.app.shop.application.port.inbound.ShopCategoryUseCase;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryPersistencePort;
import com.huijiewei.agile.app.shop.application.request.ShopCategoryRequest;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.NotFoundException;
import com.huijiewei.agile.core.until.TreeUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
public class ShopCategoryService implements ShopCategoryUseCase {
    private final ShopCategoryPersistencePort shopCategoryPersistencePort;
    private final ShopCategoryRequestMapper shopCategoryRequestMapper;
    private final ValidatingService validatingService;

    public ShopCategoryService(ShopCategoryPersistencePort shopCategoryPersistencePort, ShopCategoryRequestMapper shopCategoryRequestMapper, ValidatingService validatingService) {
        this.shopCategoryPersistencePort = shopCategoryPersistencePort;
        this.shopCategoryRequestMapper = shopCategoryRequestMapper;
        this.validatingService = validatingService;
    }

    private List<ShopCategoryEntity> getParentsById(Integer id) {
        return TreeUtils.getParents(id, this.shopCategoryPersistencePort.getAll());
    }

    @Override
    public List<ShopCategoryEntity> getTree() {
        return this.shopCategoryPersistencePort.getTree();
    }

    @Override
    public List<ShopCategoryEntity> getPath(Integer id) {
        return this.getParentsById(id);
    }

    private ShopCategoryEntity getById(Integer id) {
        return this.shopCategoryPersistencePort.getById(id).orElseThrow(() -> new NotFoundException("商品分类不存在"));
    }

    @Override
    public ShopCategoryEntity read(Integer id, Boolean withParents) {
        ShopCategoryEntity shopCategoryEntity = this.getById(id);

        if (withParents != null && withParents && shopCategoryEntity.getParentId() > 0) {
            shopCategoryEntity.setParents(this.getParentsById(shopCategoryEntity.getParentId()));
        }

        return shopCategoryEntity;
    }

    @Override
    public ShopCategoryEntity create(ShopCategoryRequest shopCategoryRequest) {
        if (!this.validatingService.validate(shopCategoryRequest)) {
            return null;
        }

        ShopCategoryEntity shopCategoryEntity = this.shopCategoryRequestMapper.toShopCategoryEntity(shopCategoryRequest);

        Integer shopCategoryId = this.shopCategoryPersistencePort.save(shopCategoryEntity);
        shopCategoryEntity.setId(shopCategoryId);

        return shopCategoryEntity;
    }

    @Override
    public ShopCategoryEntity update(Integer id, ShopCategoryRequest shopCategoryRequest) {
        ShopCategoryEntity currentShopCategoryEntity = this.getById(id);

        if (!this.validatingService.validate(shopCategoryRequest)) {
            return null;
        }

        ShopCategoryEntity shopCategoryEntity = this.shopCategoryRequestMapper.toShopCategoryEntity(shopCategoryRequest, currentShopCategoryEntity);

        Integer shopCategoryId = this.shopCategoryPersistencePort.save(shopCategoryEntity);
        shopCategoryEntity.setId(shopCategoryId);

        return shopCategoryEntity;
    }

    @Override
    public void deleteById(Integer id) {
        ShopCategoryEntity currentShopCategoryEntity = this.getById(id);

        this.shopCategoryPersistencePort.deleteById(currentShopCategoryEntity.getId());
    }
}
