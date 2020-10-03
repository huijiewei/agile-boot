package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.port.inbound.AdminHasPermissionUseCase;
import com.huijiewei.agile.app.admin.application.port.inbound.AdminUseCase;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminPersistencePort;
import com.huijiewei.agile.app.admin.application.request.AdminRequest;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.core.application.response.ListResponse;
import com.huijiewei.agile.core.exception.ConflictException;
import com.huijiewei.agile.core.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * @author huijiewei
 */

@Service
@Validated
public class AdminService implements AdminHasPermissionUseCase, AdminUseCase {
    private final AdminPersistencePort adminPersistencePort;
    private final AdminGroupPersistencePort adminGroupPersistencePort;

    @Autowired
    public AdminService(AdminPersistencePort adminPersistencePort, AdminGroupPersistencePort adminGroupPersistencePort) {
        this.adminPersistencePort = adminPersistencePort;
        this.adminGroupPersistencePort = adminGroupPersistencePort;
    }

    @Override
    public boolean hasPermissions(AdminEntity admin, String[] permissions) {
        List<String> adminGroupPermissions = this.adminGroupPersistencePort.getPermissions(admin.getAdminGroupId());

        for (String everyPermission : permissions) {
            if (adminGroupPermissions.contains(StringUtils.trim(everyPermission))) {
                return true;
            }
        }

        return false;
    }

    private AdminEntity getById(Integer id) {
        return this.adminPersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("管理员不存在"));
    }

    @Override
    public ListResponse<AdminEntity> all() {
        ListResponse<AdminEntity> response = new ListResponse<>();
        response.setItems(this.adminPersistencePort.getAll());

        return response;
    }

    @Override
    public void deleteById(Integer id, Integer identityId) {
        AdminEntity adminEntity = this.getById(id);

        if (adminEntity.getId().equals(identityId)) {
            throw new ConflictException("管理员不能删除自己");
        }

        this.adminPersistencePort.deleteById(id);
    }

    @Override
    public AdminEntity read(Integer id) {
        return this.getById(id);
    }

    @Override
    @Validated({AdminRequest.Create.class})
    public AdminEntity create(@Valid AdminRequest adminRequest) {
        return null;
    }

    @Override
    @Validated({AdminRequest.Edit.class})
    public AdminEntity update(Integer id, @Valid AdminRequest adminRequest, Integer identityId) {
        return null;
    }
}
