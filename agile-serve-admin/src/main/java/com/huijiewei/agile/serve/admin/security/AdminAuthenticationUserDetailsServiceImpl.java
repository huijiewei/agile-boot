package com.huijiewei.agile.serve.admin.security;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.app.admin.security.AdminIdentity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author huijiewei
 */

public class AdminAuthenticationUserDetailsServiceImpl implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final AdminPersistencePort adminPersistencePort;

    public AdminAuthenticationUserDetailsServiceImpl(AdminPersistencePort adminPersistencePort) {
        this.adminPersistencePort = adminPersistencePort;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws AuthenticationException {
        String clientId = (String) token.getPrincipal();
        String accessToken = (String) token.getCredentials();

        if (StringUtils.isEmpty(accessToken)) {
            throw new BadCredentialsException("无效的 AccessToken");
        }

        AdminEntity adminEntity = this.adminPersistencePort
                .getByAccessToken(accessToken, clientId)
                .orElseThrow(() -> new BadCredentialsException("无效的 AccessToken"));

        AdminIdentity adminIdentity = new AdminIdentity();
        adminIdentity.setClientId(clientId);
        adminIdentity.setAdminEntity(adminEntity);

        return new AdminUserDetails(adminIdentity);
    }
}
