package com.huijiewei.agile.serve.admin.security;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.app.admin.security.AdminIdentity;
import com.huijiewei.agile.core.until.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final AdminPersistencePort adminPersistencePort;
    private final AdminGroupPersistencePort adminGroupPersistencePort;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws AuthenticationException {
        var clientId = token.getPrincipal().toString();
        var accessToken = token.getCredentials().toString();

        if (StringUtils.isEmpty(accessToken)) {
            throw new BadCredentialsException("无效的 AccessToken");
        }

        AdminEntity adminEntity = this.adminPersistencePort
                .getByAccessToken(accessToken, clientId)
                .orElseThrow(() -> new BadCredentialsException("无效的 AccessToken"));

        AdminIdentity adminIdentity = new AdminIdentity();
        adminIdentity.setClientId(clientId);
        adminIdentity.setAdminEntity(adminEntity);
        adminIdentity.setPermissions(adminGroupPersistencePort.getPermissions(adminEntity.getAdminGroupId()));

        return new AdminUserDetails(adminIdentity);
    }
}
