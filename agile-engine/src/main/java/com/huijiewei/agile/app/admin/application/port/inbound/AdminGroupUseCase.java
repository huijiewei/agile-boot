package com.huijiewei.agile.app.admin.application.port.inbound;

import com.huijiewei.agile.app.admin.application.request.AdminGroupRequest;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.core.application.response.ListResponse;

/**
 * @author huijiewei
 */

public interface AdminGroupUseCase {
    ListResponse<AdminGroupEntity> loadAll();

    AdminGroupEntity loadById(Integer id);

    AdminGroupEntity create(AdminGroupRequest adminGroupRequest);

    AdminGroupEntity update(Integer id, AdminGroupRequest adminGroupRequest);

    void deleteById(Integer id);
}
