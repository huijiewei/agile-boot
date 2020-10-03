package com.huijiewei.agile.app.admin.application.port.inbound;

import com.huijiewei.agile.app.admin.domain.AdminEntity;

/**
 * @author huijiewei
 */

public interface AdminHasPermissionUseCase {
    /**
     * 检查权限
     *
     * @param admin       管理员
     * @param permissions 权限列表
     * @return 结果
     */
    boolean hasPermissions(AdminEntity admin, String[] permissions);
}
