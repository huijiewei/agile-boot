package com.huijiewei.agile.app.admin.application.port.outbound;

import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.application.response.PageResponse;

/**
 * @author huijiewei
 */

public interface AdminLogPersistencePort {
    PageResponse<AdminLogEntity> getAll(Integer page, Integer size);

    void save(AdminLogEntity adminLogEntity);
}
