package com.huijiewei.agile.app.shop.application.service;

import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryExistsPort;
import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
public class ShopCategoryExistsService implements ExistsUseCase {
    private final ShopCategoryExistsPort shopCategoryExistsPort;

    public ShopCategoryExistsService(ShopCategoryExistsPort shopCategoryExistsPort) {
        this.shopCategoryExistsPort = shopCategoryExistsPort;
    }

    @Override
    public Boolean exists(String targetProperty, List<String> values) {
        return this.shopCategoryExistsPort.exist(targetProperty, values);
    }
}
