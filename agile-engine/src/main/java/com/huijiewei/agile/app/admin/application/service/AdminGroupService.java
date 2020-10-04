package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.mapper.AdminGroupRequestMapper;
import com.huijiewei.agile.app.admin.application.port.inbound.AdminGroupUseCase;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminPersistencePort;
import com.huijiewei.agile.app.admin.application.request.AdminGroupRequest;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.core.application.response.ListResponse;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.ConflictException;
import com.huijiewei.agile.core.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huijiewei
 */

@Service
public class AdminGroupService implements AdminGroupUseCase {
    private final AdminPersistencePort adminPersistencePort;
    private final AdminGroupRequestMapper adminGroupRequestMapper;
    private final AdminGroupPersistencePort adminGroupPersistencePort;
    private final ValidatingService validatingService;

    @Autowired
    public AdminGroupService(AdminPersistencePort adminPersistencePort, AdminGroupRequestMapper adminGroupRequestMapper, AdminGroupPersistencePort adminGroupPersistencePort, ValidatingService validatingService) {
        this.adminPersistencePort = adminPersistencePort;
        this.adminGroupRequestMapper = adminGroupRequestMapper;
        this.adminGroupPersistencePort = adminGroupPersistencePort;
        this.validatingService = validatingService;
    }

    @Override
    public void deleteById(Integer id) {
        AdminGroupEntity adminGroupEntity = this.getById(id);

        if (adminPersistencePort.existsByAdminGroupId(adminGroupEntity.getId())) {
            throw new ConflictException("管理组内拥有管理员，无法删除");
        }

        this.adminGroupPersistencePort.deleteById(adminGroupEntity.getId());
        this.adminGroupPersistencePort.updatePermissions(id, null, true);
    }

    @Override
    public ListResponse<AdminGroupEntity> all() {
        ListResponse<AdminGroupEntity> response = new ListResponse<>();
        response.setItems(this.adminGroupPersistencePort.getAll());

        return response;
    }

    @Override
    public AdminGroupEntity read(Integer id) {
        AdminGroupEntity adminGroupEntity = this.getById(id);
        adminGroupEntity.setPermissions(this.adminGroupPersistencePort.getPermissions(id));

        return adminGroupEntity;
    }

    private AdminGroupEntity getById(Integer id) {
        return this.adminGroupPersistencePort.getById(id).orElseThrow(() -> new NotFoundException("管理组不存在"));
    }

    @Override
    public AdminGroupEntity create(AdminGroupRequest adminGroupRequest) {
        if (!this.validatingService.validate(adminGroupRequest)) {
            return null;
        }

        AdminGroupEntity adminGroupEntity = this.adminGroupRequestMapper.toAdminGroupEntity(adminGroupRequest);

        if (!this.validatingService.validate(adminGroupEntity)) {
            return null;
        }

        Integer adminGroupId = this.adminGroupPersistencePort.save(adminGroupEntity);

        this.adminGroupPersistencePort.updatePermissions(adminGroupId, adminGroupRequest.getPermissions(), false);

        adminGroupEntity.setId(adminGroupId);

        return adminGroupEntity;
    }

    @Override
    public AdminGroupEntity update(Integer id, AdminGroupRequest adminGroupRequest) {
        if (!this.validatingService.validate(adminGroupRequest)) {
            return null;
        }

        AdminGroupEntity adminGroupEntity = this.adminGroupRequestMapper.toAdminGroupEntity(adminGroupRequest, this.getById(id));

        if (!this.validatingService.validate(adminGroupEntity)) {
            return null;
        }

        Integer adminGroupId = this.adminGroupPersistencePort.save(adminGroupEntity);

        this.adminGroupPersistencePort.updatePermissions(adminGroupId, adminGroupRequest.getPermissions(), true);

        adminGroupEntity.setId(adminGroupId);

        return adminGroupEntity;
    }
}
