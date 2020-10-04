package com.huijiewei.agile.app.admin.adapter.persistence;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminGroup;
import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminGroupMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.JpaAdminGroupRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupExistsPort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupUniquePort;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.app.admin.security.AdminGroupMenuItem;
import com.huijiewei.agile.app.admin.security.AdminGroupMenus;
import com.huijiewei.agile.core.adapter.persistence.ExistsSpecificationBuilder;
import com.huijiewei.agile.core.adapter.persistence.UniqueSpecificationBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
class JpaAdminGroupAdapter implements AdminGroupPersistencePort, AdminGroupExistsPort, AdminGroupUniquePort {
    private final AdminGroupMapper adminGroupMapper;
    private final JpaAdminGroupRepository jpaAdminGroupRepository;
    private final JpaAdminGroupCacheAdapter jpaAdminGroupCacheAdapter;

    public JpaAdminGroupAdapter(AdminGroupMapper adminGroupMapper, JpaAdminGroupRepository jpaAdminGroupRepository, JpaAdminGroupCacheAdapter jpaAdminGroupCacheAdapter) {
        this.adminGroupMapper = adminGroupMapper;
        this.jpaAdminGroupRepository = jpaAdminGroupRepository;
        this.jpaAdminGroupCacheAdapter = jpaAdminGroupCacheAdapter;
    }

    @Override
    public Optional<AdminGroupEntity> getById(Integer id) {
        return this.jpaAdminGroupRepository.findById(id).map(this.adminGroupMapper::toAdminGroupEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(AdminGroupEntity adminGroupEntity) {
        AdminGroup adminGroup = this.jpaAdminGroupRepository.save(this.adminGroupMapper.toAdminGroup(adminGroupEntity));

        this.jpaAdminGroupCacheAdapter.updatePermissions(adminGroup.getId(), adminGroupEntity.getPermissions(), adminGroupEntity.hasId());

        return adminGroup.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.jpaAdminGroupRepository.deleteById(id);
        this.jpaAdminGroupCacheAdapter.updatePermissions(id, null, true);
    }

    @Override
    public List<String> getPermissions(Integer id) {
        return this.jpaAdminGroupCacheAdapter.getPermissions(id);
    }

    @Override
    @Cacheable(cacheNames = JpaAdminGroupCacheAdapter.ADMIN_GROUP_MENUS_CACHE_KEY, key = "#id")
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
    public Boolean exists(String targetProperty, List<String> values) {
        return this.jpaAdminGroupRepository.count(ExistsSpecificationBuilder.build(targetProperty, values)) > 0;
    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.jpaAdminGroupRepository.count(UniqueSpecificationBuilder.build(values, primaryKey, primaryValue)) == 0;
    }
}
