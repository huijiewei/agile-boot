package com.huijiewei.agile.app.admin.adapter.persistence;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminGroupPermission;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.AdminGroupPermissionRepository;
import com.huijiewei.agile.core.until.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@RequiredArgsConstructor
public class AdminGroupCacheAdapter {
    public static final String ADMIN_GROUP_PERMISSIONS_CACHE_KEY = "admin-group-permissions";
    public static final String ADMIN_GROUP_MENUS_CACHE_KEY = "admin-group-menus";

    private final AdminGroupPermissionRepository jpaAdminGroupPermissionRepository;

    @Cacheable(cacheNames = ADMIN_GROUP_PERMISSIONS_CACHE_KEY, key = "#id")
    public List<String> getPermissions(Integer id) {
        return this.jpaAdminGroupPermissionRepository
                .findByAdminGroupId(id)
                .stream()
                .map(AdminGroupPermission::getActionId)
                .collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = {ADMIN_GROUP_MENUS_CACHE_KEY, ADMIN_GROUP_PERMISSIONS_CACHE_KEY}, key = "#id")
    public void updatePermissions(Integer id, List<String> permissions, Boolean delete) {
        if (delete) {
            this.jpaAdminGroupPermissionRepository.deleteByAdminGroupId(id);
        }

        if (CollectionUtils.isEmpty(permissions)) {
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
}
