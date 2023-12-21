package com.huijiewei.agile.app.admin.adapter.persistence;

import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminGroupMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.AdminGroupRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupExistsPort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupUniquePort;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.app.admin.security.AdminGroupMenuItem;
import com.huijiewei.agile.app.admin.security.AdminGroupMenus;
import com.huijiewei.agile.core.adapter.persistence.JpaSpecificationBuilder;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
class AdminGroupAdapter implements AdminGroupPersistencePort, AdminGroupExistsPort, AdminGroupUniquePort {
    private final AdminGroupMapper adminGroupMapper;
    private final AdminGroupRepository adminGroupRepository;
    private final AdminGroupCacheAdapter adminGroupCacheAdapter;

    @Override
    public Optional<AdminGroupEntity> getById(Integer id) {
        return this.adminGroupRepository.findById(id).map(this.adminGroupMapper::toAdminGroupEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(AdminGroupEntity adminGroupEntity) {
        var adminGroup = this.adminGroupRepository.save(this.adminGroupMapper.toAdminGroup(adminGroupEntity));

        this.adminGroupCacheAdapter.updatePermissions(adminGroup.getId(), adminGroupEntity.getPermissions(), adminGroupEntity.hasId());

        return adminGroup.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.adminGroupRepository.deleteById(id);
        this.adminGroupCacheAdapter.updatePermissions(id, null, true);
    }

    @Override
    public List<String> getPermissions(Integer id) {
        return this.adminGroupCacheAdapter.getPermissions(id);
    }

    @Override
    @Cacheable(cacheNames = AdminGroupCacheAdapter.ADMIN_GROUP_MENUS_CACHE_KEY, key = "#a0")
    public List<AdminGroupMenuItem> getMenus(Integer id) {
        var all = AdminGroupMenus.getAll();
        var adminGroupPermissions = this.getPermissions(id);

        var adminGroupMenuItems = new ArrayList<AdminGroupMenuItem>();

        for (var adminGroupMenuItem : all) {
            var item = AdminGroupMenus.getAdminGroupMenuItemInPermissions(adminGroupMenuItem, adminGroupPermissions);

            if (item != null) {
                adminGroupMenuItems.add(item);
            }
        }

        return adminGroupMenuItems;
    }

    @Override
    public List<AdminGroupEntity> getAll() {
        return this.adminGroupRepository
                .findAll()
                .stream()
                .map(this.adminGroupMapper::toAdminGroupEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean exists(String targetProperty, List<String> values) {
        return this.adminGroupRepository.count(JpaSpecificationBuilder.buildExists(targetProperty, values)) > 0;
    }

    @Override
    public boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.adminGroupRepository.count(JpaSpecificationBuilder.buildUnique(values, primaryKey, primaryValue)) == 0;
    }
}
