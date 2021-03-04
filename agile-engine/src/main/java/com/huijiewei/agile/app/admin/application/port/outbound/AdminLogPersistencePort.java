package com.huijiewei.agile.app.admin.application.port.outbound;

import com.huijiewei.agile.app.admin.application.request.AdminLogSearchRequest;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

/**
 * @author huijiewei
 */

public interface AdminLogPersistencePort {
    SearchPageResponse<AdminLogEntity> getAll(AdminLogSearchRequest searchRequest, PageRequest pageRequest, Boolean withSearchFields);

    void save(AdminLogEntity adminLogEntity);
}
