package com.huijiewei.agile.app.admin.application.port.outbound;

import com.huijiewei.agile.app.admin.application.request.AdminLogSearchRequest;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

/**
 * @author huijiewei
 */

public interface AdminLogPersistencePort {
    SearchPageResponse<AdminLogEntity> getAll(Integer page, Integer size, AdminLogSearchRequest searchRequest, Boolean withSearchFields);

    void save(AdminLogEntity adminLogEntity);
}
