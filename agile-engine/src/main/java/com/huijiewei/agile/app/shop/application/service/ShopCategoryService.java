package com.huijiewei.agile.app.shop.application.service;

import com.huijiewei.agile.app.shop.application.mapper.ShopCategoryRequestMapper;
import com.huijiewei.agile.app.shop.application.port.inbound.ShopCategoryUseCase;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryPersistencePort;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopProductPersistencePort;
import com.huijiewei.agile.app.shop.application.request.ShopCategoryRequest;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.ConflictException;
import com.huijiewei.agile.core.exception.NotFoundException;
import com.huijiewei.agile.core.until.TreeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class ShopCategoryService implements ShopCategoryUseCase {
    private final ShopCategoryPersistencePort shopCategoryPersistencePort;
    private final ShopCategoryRequestMapper shopCategoryRequestMapper;
    private final ValidatingService validatingService;
    private final ShopProductPersistencePort shopProductPersistencePort;

    private List<ShopCategoryEntity> getParentsById(Integer id) {
        return TreeUtils.getParents(id, this.shopCategoryPersistencePort.getAll());
    }

    @Override
    public List<ShopCategoryEntity> loadTree() {
        return this.shopCategoryPersistencePort.getTree();
    }

    @Override
    public List<ShopCategoryEntity> loadPathById(Integer id) {
        return this.getParentsById(id);
    }

    private ShopCategoryEntity getById(Integer id) {
        return this.shopCategoryPersistencePort.getById(id).orElseThrow(() -> new NotFoundException("商品分类不存在"));
    }

    @Override
    public ShopCategoryEntity loadById(Integer id, Boolean withParents) {
        var shopCategoryEntity = this.getById(id);

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

        var shopCategoryEntity = this.shopCategoryRequestMapper.toShopCategoryEntity(shopCategoryRequest);

        var shopCategoryId = this.shopCategoryPersistencePort.save(shopCategoryEntity);
        shopCategoryEntity.setId(shopCategoryId);

        return shopCategoryEntity;
    }

    @Override
    public ShopCategoryEntity update(Integer id, ShopCategoryRequest shopCategoryRequest) {
        var shopCategoryEntity = this.getById(id);

        if (!this.validatingService.validate(shopCategoryRequest)) {
            return null;
        }

        this.shopCategoryRequestMapper.updateShopCategoryEntity(shopCategoryRequest, shopCategoryEntity);

        if (!this.validatingService.validate(shopCategoryEntity)) {
            return null;
        }

        var shopCategoryId = this.shopCategoryPersistencePort.save(shopCategoryEntity);
        shopCategoryEntity.setId(shopCategoryId);

        shopCategoryEntity.setParents(this.getParentsById(shopCategoryEntity.getParentId()));

        return shopCategoryEntity;
    }

    @Override
    public void deleteById(Integer id) {
        var shopCategoryEntity = this.getById(id);

        var deleteIds = TreeUtils.getChildrenIds(shopCategoryEntity.getId(), this.shopCategoryPersistencePort.getTree());
        deleteIds.add(shopCategoryEntity.getId());

        if (this.shopProductPersistencePort.existsByShopCategoryIds(deleteIds)) {
            throw new ConflictException("商品分类内拥有商品，无法删除");
        }

        this.shopCategoryPersistencePort.deleteAllById(deleteIds);
    }
}
