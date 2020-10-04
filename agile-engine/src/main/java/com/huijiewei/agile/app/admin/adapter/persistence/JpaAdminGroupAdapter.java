package com.huijiewei.agile.app.admin.adapter.persistence;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminGroup;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminGroupPermission;
import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminGroupMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.JpaAdminGroupPermissionRepository;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.JpaAdminGroupRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupExistsPort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupUniquePort;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.app.admin.security.AdminGroupMenuItem;
import com.huijiewei.agile.app.admin.security.AdminGroupMenus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
class JpaAdminGroupAdapter implements AdminGroupPersistencePort, AdminGroupExistsPort, AdminGroupUniquePort {
    private static final String ADMIN_GROUP_MENUS_CACHE_KEY = "admin-group-menus";
    private static final String ADMIN_GROUP_PERMISSIONS_CACHE_KEY = "admin-group-permissions";

    private final AdminGroupMapper adminGroupMapper;
    private final JpaAdminGroupRepository jpaAdminGroupRepository;
    private final JpaAdminGroupPermissionRepository jpaAdminGroupPermissionRepository;

    @Autowired
    public JpaAdminGroupAdapter(AdminGroupMapper adminGroupMapper, JpaAdminGroupRepository jpaAdminGroupRepository, JpaAdminGroupPermissionRepository jpaAdminGroupPermissionRepository) {
        this.adminGroupMapper = adminGroupMapper;
        this.jpaAdminGroupRepository = jpaAdminGroupRepository;
        this.jpaAdminGroupPermissionRepository = jpaAdminGroupPermissionRepository;
    }

    @Override
    public Optional<AdminGroupEntity> getById(Integer id) {
        return this.jpaAdminGroupRepository.findById(id).map(this.adminGroupMapper::toAdminGroupEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(AdminGroupEntity adminGroupEntity) {
        AdminGroup adminGroup = this.jpaAdminGroupRepository.save(this.adminGroupMapper.toAdminGroup(adminGroupEntity));

        return adminGroup.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.jpaAdminGroupRepository.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = ADMIN_GROUP_PERMISSIONS_CACHE_KEY, key = "#id")
    public List<String> getPermissions(Integer id) {
        return this.jpaAdminGroupPermissionRepository
                .findAllByAdminGroupId(id)
                .stream()
                .map(AdminGroupPermission::getActionId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {ADMIN_GROUP_MENUS_CACHE_KEY, ADMIN_GROUP_PERMISSIONS_CACHE_KEY}, key = "#id")
    public void updatePermissions(Integer id, List<String> permissions, Boolean delete) {
        if (delete) {
            this.jpaAdminGroupPermissionRepository.deleteByAdminGroupId(id);
        }

        if (permissions == null) {
            return;
        }

        if (permissions.isEmpty()) {
            return;
        }

        List<AdminGroupPermission> adminGroupPermissions = new ArrayList<>();

        for (String actionId : permissions) {
            AdminGroupPermission permission = new AdminGroupPermission();
            permission.setActionId(actionId);
            permission.setAdminGroupId(id);

            adminGroupPermissions.add(permission);
        }

        this.jpaAdminGroupPermissionRepository.batchInsert(adminGroupPermissions);
    }

    @Override
    @Cacheable(cacheNames = ADMIN_GROUP_MENUS_CACHE_KEY, key = "#id")
    public List<AdminGroupMenuItem> getMenus(Integer id) {
        List<AdminGroupMenuItem> all = AdminGroupMenus.getAll();
        List<String> adminGroupPermissions = this.getPermissions(id);

        List<AdminGroupMenuItem> adminGroupMenuItems = new ArrayList<>();

        for (AdminGroupMenuItem adminGroupMenuItem : all) {
            AdminGroupMenuItem item = AdminGroupMenus.getAdminGroupMenuItemInPermissions(adminGroupMenuItem, adminGroupPermissions);

            if (item != null) {
                adminGroupMenuItems.add(item);
            }
        }

        return adminGroupMenuItems;
    }

    @Override
    public List<AdminGroupEntity> getAll() {
        return this.jpaAdminGroupRepository
                .findAll()
                .stream()
                .map(this.adminGroupMapper::toAdminGroupEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean exist(String targetProperty, List<String> values) {
        Specification<AdminGroup> adminGroupSpecification = (Specification<AdminGroup>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            for (String value : values) {
                predicates.add(criteriaBuilder.equal(root.get(targetProperty), value));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return this.jpaAdminGroupRepository.count(adminGroupSpecification) > 0;
    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        Specification<AdminGroup> adminGroupSpecification = (Specification<AdminGroup>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            for (Map.Entry<String, String> entry : values.entrySet()) {
                predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
            }

            if (StringUtils.isNotEmpty(primaryValue)) {
                predicates.add(criteriaBuilder.notEqual(root.get(primaryKey), primaryValue));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return this.jpaAdminGroupRepository.count(adminGroupSpecification) == 0;
    }
}
