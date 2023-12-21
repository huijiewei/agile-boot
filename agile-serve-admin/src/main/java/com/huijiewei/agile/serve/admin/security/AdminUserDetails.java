package com.huijiewei.agile.serve.admin.security;

import com.huijiewei.agile.app.admin.security.AdminIdentity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

public class AdminUserDetails implements UserDetails {
    @Getter
    private final AdminIdentity adminIdentity;
    private final List<GrantedAuthority> authorities;

    AdminUserDetails(AdminIdentity adminIdentity) {

        this.adminIdentity = adminIdentity;
        this.authorities = adminIdentity.getPermissions()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.adminIdentity.getAdminEntity().getName();
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
