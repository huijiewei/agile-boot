package com.huijiewei.agile.app.admin.application.port.inbound;

import com.huijiewei.agile.app.admin.application.request.AdminRequest;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.core.application.response.ListResponse;

/**
 * @author huijiewei
 */

public interface AdminUseCase {
    ListResponse<AdminEntity> loadAll();

    AdminEntity loadById(Integer id);

    AdminEntity create(AdminRequest adminRequest);

    AdminEntity update(Integer id, AdminRequest adminRequest, Integer identityId);

    void deleteById(Integer id, Integer identityId);
}
