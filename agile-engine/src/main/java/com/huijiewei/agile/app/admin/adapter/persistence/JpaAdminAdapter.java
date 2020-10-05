package com.huijiewei.agile.app.admin.adapter.persistence;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.Admin;
import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.JpaAdminRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminUniquePort;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.core.adapter.persistence.UniqueSpecificationBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
class JpaAdminAdapter implements AdminPersistencePort, AdminUniquePort {
    private final AdminMapper adminMapper;
    private final JpaAdminRepository adminRepository;

    public JpaAdminAdapter(AdminMapper adminMapper, JpaAdminRepository adminRepository) {
        this.adminMapper = adminMapper;
        this.adminRepository = adminRepository;
    }

    @Override
    public Optional<AdminEntity> getById(Integer id) {
        return this.adminRepository.findById(id).map(this.adminMapper::toAdminEntity);
    }

    @Override
    public Optional<AdminEntity> getByIdWithAdminGroup(Integer id) {
        return this.adminRepository
                .findById(id, EntityGraphUtils.fromAttributePaths("adminGroup"))
                .map(this.adminMapper::toAdminEntityWithAdminGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(AdminEntity adminEntity) {
        Admin admin = this.adminRepository.save(this.adminMapper.toAdmin(adminEntity));

        return admin.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.adminRepository.deleteById(id);
    }

    @Override
    public List<AdminEntity> getAll() {
        return this.adminRepository
                .findAll()
                .stream()
                .map(this.adminMapper::toAdminEntityWithAdminGroup)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AdminEntity> getByAccessToken(String accessToken, String clientId) {
        return this.adminRepository.findByAccessToken(accessToken, clientId).map(this.adminMapper::toAdminEntityWithAdminGroup);
    }

    @Override
    public Optional<AdminEntity> getByPhone(String phone) {
        return this.adminRepository.findByPhone(phone).map(this.adminMapper::toAdminEntityWithAdminGroup);
    }

    @Override
    public Optional<AdminEntity> getByEmail(String email) {
        return this.adminRepository.findByEmail(email).map(this.adminMapper::toAdminEntityWithAdminGroup);
    }

    @Override
    public Boolean existsByAdminGroupId(Integer adminGroupId) {
        return this.adminRepository.existsByAdminGroupId(adminGroupId);
    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.adminRepository.count(UniqueSpecificationBuilder.build(values, primaryKey, primaryValue)) == 0;
    }
}
