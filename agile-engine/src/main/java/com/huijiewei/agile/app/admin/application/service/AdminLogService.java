package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.port.inbound.AdminLogUseCase;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.application.request.AdminLogSearchRequest;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class AdminLogService implements AdminLogUseCase {
    private final AdminLogPersistencePort adminLogPersistencePort;

    @Override
    public SearchPageResponse<AdminLogEntity> search(AdminLogSearchRequest searchRequest, PageRequest pageRequest, Boolean withSearchFields) {
        return this.adminLogPersistencePort.getAll(searchRequest, pageRequest, withSearchFields);
    }
}
