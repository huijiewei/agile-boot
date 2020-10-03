package com.huijiewei.agile.app.admin.application.port.inbound;

import com.huijiewei.agile.app.admin.application.request.AdminGroupRequest;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.core.application.response.ListResponse;

import javax.validation.Valid;

/**
 * @author huijiewei
 */

public interface AdminGroupUseCase {
    ListResponse<AdminGroupEntity> all();

    AdminGroupEntity read(Integer id);

    AdminGroupEntity create(@Valid AdminGroupRequest adminGroupRequest);

    AdminGroupEntity update(Integer id, @Valid AdminGroupRequest adminGroupRequest);

    void deleteById(Integer id);
}
