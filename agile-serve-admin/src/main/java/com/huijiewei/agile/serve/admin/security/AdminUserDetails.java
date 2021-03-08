package com.huijiewei.agile.serve.admin.security;

import com.huijiewei.agile.app.admin.security.AdminIdentity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

public class AdminUserDetails implements UserDetails {
    private final AdminIdentity adminIdentity;

    AdminUserDetails(AdminIdentity adminIdentity) {
        this.adminIdentity = adminIdentity;
    }

    public static AdminIdentity getCurrentAdminIdentity() {
        var adminUserDetails = (AdminUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return adminUserDetails.getAdminIdentity();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getAdminIdentity()
                .getPermissions()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.adminIdentity.getAdminEntity().getName();
    }

    public AdminIdentity getAdminIdentity() {
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
