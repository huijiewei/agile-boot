package com.huijiewei.agile.app.admin.application.port.inbound;

import com.huijiewei.agile.app.admin.application.request.AdminRequest;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.core.application.response.ListResponse;

import javax.validation.Valid;

/**
 * @author huijiewei
 */

public interface AdminUseCase {
    ListResponse<AdminEntity> all();

    AdminEntity read(Integer id);

    AdminEntity create(@Valid AdminRequest adminRequest);

    AdminEntity update(Integer id, @Valid AdminRequest adminRequest, Integer identityId);

    void deleteById(Integer id, Integer identityId);
}
