package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminUniquePort;
import com.huijiewei.agile.core.application.port.inbound.UniqueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class AdminUniqueService implements UniqueUseCase {
    private final AdminUniquePort adminUniquePort;

    @Override
    public boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.adminUniquePort.unique(values, primaryKey, primaryValue);
    }
}
