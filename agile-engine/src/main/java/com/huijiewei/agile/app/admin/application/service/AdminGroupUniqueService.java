package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupUniquePort;
import com.huijiewei.agile.core.application.port.inbound.UniqueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class AdminGroupUniqueService implements UniqueUseCase {
    private final AdminGroupUniquePort adminGroupUniquePort;

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.adminGroupUniquePort.unique(values, primaryKey, primaryValue);
    }
}
