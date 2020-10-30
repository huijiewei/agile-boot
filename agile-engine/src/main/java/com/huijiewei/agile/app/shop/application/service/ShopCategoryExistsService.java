package com.huijiewei.agile.app.shop.application.service;

import com.huijiewei.agile.app.shop.application.port.outbound.ShopCategoryExistsPort;
import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class ShopCategoryExistsService implements ExistsUseCase {
    private final ShopCategoryExistsPort shopCategoryExistsPort;

    @Override
    public Boolean exists(String targetProperty, List<String> values) {
        return this.shopCategoryExistsPort.exists(targetProperty, values);
    }
}
