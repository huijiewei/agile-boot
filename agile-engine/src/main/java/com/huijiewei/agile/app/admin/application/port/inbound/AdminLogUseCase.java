package com.huijiewei.agile.app.admin.application.port.inbound;

import com.huijiewei.agile.app.admin.application.request.AdminLogSearchRequest;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

/**
 * @author huijiewei
 */

public interface AdminLogUseCase {
    SearchPageResponse<AdminLogEntity> search(AdminLogSearchRequest searchRequest, PageRequest pageRequest, Boolean withSearchFields);
}
