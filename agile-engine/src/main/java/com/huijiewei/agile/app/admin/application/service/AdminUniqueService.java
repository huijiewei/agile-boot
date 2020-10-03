package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminUniquePort;
import com.huijiewei.agile.core.application.port.inbound.UniqueUseCase;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author huijiewei
 */

@Service
public class AdminUniqueService implements UniqueUseCase {
    private final AdminUniquePort adminUniquePort;

    public AdminUniqueService(AdminUniquePort adminUniquePort) {
        this.adminUniquePort = adminUniquePort;
    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.adminUniquePort.unique(values, primaryKey, primaryValue);
    }
}
