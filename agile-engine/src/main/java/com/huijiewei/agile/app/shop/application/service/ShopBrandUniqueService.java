package com.huijiewei.agile.app.shop.application.service;

import com.huijiewei.agile.app.shop.application.port.outbound.ShopBrandUniquePort;
import com.huijiewei.agile.core.application.port.inbound.UniqueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class ShopBrandUniqueService implements UniqueUseCase {
    private final ShopBrandUniquePort shopBrandUniquePort;

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.shopBrandUniquePort.unique(values, primaryKey, primaryValue);
    }
}
