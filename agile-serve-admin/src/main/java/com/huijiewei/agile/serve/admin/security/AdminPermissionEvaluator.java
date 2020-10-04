package com.huijiewei.agile.serve.admin.security;

import com.huijiewei.agile.app.admin.application.port.inbound.AdminHasPermissionUseCase;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author huijiewei
 */

@Component
public class AdminPermissionEvaluator implements PermissionEvaluator {
    private final AdminHasPermissionUseCase adminHasPermissionUseCase;

    public AdminPermissionEvaluator(AdminHasPermissionUseCase adminHasPermissionUseCase) {
        this.adminHasPermissionUseCase = adminHasPermissionUseCase;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null) {
            return false;
        }

        AdminUserDetails adminUserDetails = (AdminUserDetails) authentication.getPrincipal();

        if (adminUserDetails == null) {
            return false;
        }

        return this.adminHasPermissionUseCase.hasPermissions(adminUserDetails.getAdminIdentity().getAdminEntity(), permission.toString().split(","));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new RuntimeException("Id and Class permissions are not supported by this application");
    }
}
