package com.huijiewei.agile.app.admin.adapter.persistence.repository;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminGroupPermission;
import com.huijiewei.agile.core.adapter.persistence.BatchJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huijiewei
 */

@Repository
public interface AdminGroupPermissionRepository extends
        BatchJpaRepository<AdminGroupPermission>,
        JpaRepository<AdminGroupPermission, Integer> {
    /**
     * 获取管理组权限列表
     *
     * @param adminGroupId adminGroupId
     * @return 管理组权限列表
     */
    List<AdminGroupPermission> findByAdminGroupId(Integer adminGroupId);

    void deleteByAdminGroupId(Integer adminGroupId);
}
