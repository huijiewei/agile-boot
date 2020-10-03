package com.huijiewei.agile.app.admin.adapter.persistence;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.Admin;
import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.JpaAdminRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminUniquePort;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
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

    @Autowired
    public JpaAdminAdapter(AdminMapper adminMapper, JpaAdminRepository adminRepository) {
        this.adminMapper = adminMapper;
        this.adminRepository = adminRepository;
    }

    @Override
    public Optional<AdminEntity> getById(Integer id) {
        return this.adminRepository.findById(id).map(this.adminMapper::toAdminEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AdminEntity adminEntity) {
        Admin admin = this.adminRepository.save(this.adminMapper.toAdmin(adminEntity));

        adminEntity.setId(admin.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.adminRepository.deleteById(id);
    }

    @Override
    public List<AdminEntity> getAll() {
        return this.adminRepository.findAll().stream().map(this.adminMapper::toAdminEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<AdminEntity> getByAccessToken(String accessToken, String clientId) {
        return this.adminRepository.findByAccessToken(accessToken, clientId).map(this.adminMapper::toAdminEntity);
    }

    @Override
    public Optional<AdminEntity> getByPhone(String phone) {
        return this.adminRepository.findByPhone(phone).map(this.adminMapper::toAdminEntity);
    }

    @Override
    public Optional<AdminEntity> getByEmail(String email) {
        return this.adminRepository.findByEmail(email).map(this.adminMapper::toAdminEntity);
    }

    @Override
    public Boolean existsByAdminGroupId(Integer adminGroupId) {
        return this.adminRepository.existsByAdminGroupId(adminGroupId);
    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        Specification<Admin> adminSpecification = (Specification<Admin>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            for (Map.Entry<String, String> entry : values.entrySet()) {
                predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
            }

            if (StringUtils.isNotEmpty(primaryValue)) {
                predicates.add(criteriaBuilder.notEqual(root.get(primaryKey), primaryValue));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return this.adminRepository.count(adminSpecification) == 0;
    }
}
