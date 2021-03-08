package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.mapper.AdminRequestMapper;
import com.huijiewei.agile.app.admin.application.port.inbound.AdminUseCase;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminPersistencePort;
import com.huijiewei.agile.app.admin.application.request.AdminRequest;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.core.application.response.ListResponse;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.ConflictException;
import com.huijiewei.agile.core.exception.NotFoundException;
import com.huijiewei.agile.core.until.SecurityUtils;
import com.huijiewei.agile.core.until.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class AdminService implements AdminUseCase {
    private final AdminPersistencePort adminPersistencePort;
    private final ValidatingService validatingService;
    private final AdminRequestMapper adminRequestMapper;

    private AdminEntity getById(Integer id) {
        return this.adminPersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("管理员不存在"));
    }

    @Override
    public ListResponse<AdminEntity> loadAll() {
        var response = new ListResponse<AdminEntity>();
        response.setItems(this.adminPersistencePort.getAll());

        return response;
    }

    @Override
    public void deleteById(Integer id, Integer identityId) {
        var adminEntity = this.getById(id);

        if (adminEntity.getId().equals(identityId)) {
            throw new ConflictException("管理员不能删除自己");
        }

        this.adminPersistencePort.deleteById(id);
    }

    @Override
    public AdminEntity loadById(Integer id) {
        return this.getById(id);
    }

    @Override
    public AdminEntity create(AdminRequest adminRequest) {
        if (!this.validatingService.validate(adminRequest, AdminRequest.OnCreate.class)) {
            return null;
        }

        var adminEntity = this.adminRequestMapper.toAdminEntity(adminRequest);
        adminEntity.setPassword(SecurityUtils.passwordEncode(adminRequest.getPassword()));

        if (!this.validatingService.validate(adminEntity)) {
            return null;
        }

        var adminId = this.adminPersistencePort.save(adminEntity);
        adminEntity.setId(adminId);

        return adminEntity;
    }

    @Override
    public AdminEntity update(Integer id, AdminRequest adminRequest, Integer identityId) {
        var adminEntity = this.getById(id);

        if (!this.validatingService.validate(adminRequest, AdminRequest.OnUpdate.class)) {
            return null;
        }

        if (id.equals(identityId)) {
            adminRequest.setAdminGroupId(null);
        }

        this.adminRequestMapper.updateAdminEntity(adminRequest, adminEntity);

        if (StringUtils.isNotEmpty(adminRequest.getPassword())) {
            adminEntity.setPassword(SecurityUtils.passwordEncode(adminRequest.getPassword()));
        }

        if (!this.validatingService.validate(adminEntity)) {
            return null;
        }

        var adminId = this.adminPersistencePort.save(adminEntity);
        adminEntity.setId(adminId);

        return adminEntity;
    }
}
