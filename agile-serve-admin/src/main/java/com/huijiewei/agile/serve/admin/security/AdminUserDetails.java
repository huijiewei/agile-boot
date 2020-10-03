package com.huijiewei.agile.serve.admin.security;

import com.huijiewei.agile.app.admin.security.AdminIdentity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author huijiewei
 */

public class AdminUserDetails implements UserDetails {
    private final AdminIdentity adminIdentity;

    AdminUserDetails(AdminIdentity adminIdentity) {
        this.adminIdentity = adminIdentity;
    }

    public static AdminIdentity getCurrentAdminIdentity() {
        AdminUserDetails adminUserDetails = (AdminUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return adminUserDetails.getAdminIdentity();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.adminIdentity.getAdminEntity().getName();
    }

    AdminIdentity getAdminIdentity() {
        return this.adminIdentity;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
