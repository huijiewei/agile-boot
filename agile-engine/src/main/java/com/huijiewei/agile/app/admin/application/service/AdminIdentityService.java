package com.huijiewei.agile.app.admin.application.service;

import com.devskiller.friendly_id.FriendlyId;
import com.huijiewei.agile.app.admin.application.port.inbound.AdminIdentityUseCase;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminAccessTokenPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.application.request.AdminLoginRequest;
import com.huijiewei.agile.app.admin.application.response.AdminIdentityResponse;
import com.huijiewei.agile.app.admin.domain.AdminAccessTokenEntity;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.app.admin.security.AdminIdentity;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.consts.IdentityLogStatus;
import com.huijiewei.agile.core.consts.IdentityLogType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class AdminIdentityService implements AdminIdentityUseCase {
    private final AdminLogPersistencePort adminLogPersistencePort;
    private final AdminGroupPersistencePort adminGroupPersistencePort;
    private final AdminAccessTokenPersistencePort adminAccessTokenPersistencePort;
    private final ValidatingService validatingService;

    @Override
    public AdminIdentityResponse login(AdminLoginRequest loginRequest) {
        if (!this.validatingService.validate(loginRequest)) {
            return null;
        }

        AdminEntity adminEntity = (AdminEntity) loginRequest.getIdentity();
        String accessToken = FriendlyId.createFriendlyId();

        AdminAccessTokenEntity adminAccessTokenEntity = this.adminAccessTokenPersistencePort
                .getByAdminIdAndClientId(adminEntity.getId(), loginRequest.getClientId())
                .orElseGet(AdminAccessTokenEntity::new);

        if (!adminAccessTokenEntity.hasId()) {
            adminAccessTokenEntity.setAdminId(adminEntity.getId());
            adminAccessTokenEntity.setClientId(loginRequest.getClientId());
        }

        adminAccessTokenEntity.setUserAgent(loginRequest.getUserAgent());
        adminAccessTokenEntity.setRemoteAddr(loginRequest.getRemoteAddr());
        adminAccessTokenEntity.setAccessToken(accessToken);

        this.adminAccessTokenPersistencePort.save(adminAccessTokenEntity);

        AdminIdentityResponse adminIdentityResponse = new AdminIdentityResponse();
        adminIdentityResponse.setCurrentUser(adminEntity);
        adminIdentityResponse.setGroupPermissions(this.adminGroupPersistencePort.getPermissions(adminEntity.getAdminGroupId()));
        adminIdentityResponse.setGroupMenus(this.adminGroupPersistencePort.getMenus(adminEntity.getAdminGroupId()));
        adminIdentityResponse.setAccessToken(accessToken);

        return adminIdentityResponse;
    }

    @Override
    public AdminIdentityResponse account(AdminIdentity adminIdentity) {
        AdminEntity adminEntity = adminIdentity.getAdminEntity();

        AdminIdentityResponse adminIdentityResponse = new AdminIdentityResponse();
        adminIdentityResponse.setCurrentUser(adminEntity);

        adminIdentityResponse.setGroupPermissions(this.adminGroupPersistencePort.getPermissions(adminEntity.getAdminGroupId()));
        adminIdentityResponse.setGroupMenus(this.adminGroupPersistencePort.getMenus(adminEntity.getAdminGroupId()));

        return adminIdentityResponse;
    }

    @Override
    public void logout(AdminIdentity adminIdentity, String userAgent, String remoteAddr) {
        this.adminAccessTokenPersistencePort.delete(adminIdentity.getAdminEntity().getId(), adminIdentity.getClientId());

        AdminLogEntity adminLogEntity = new AdminLogEntity();
        adminLogEntity.setAdminId(adminIdentity.getAdminEntity().getId());
        adminLogEntity.setType(IdentityLogType.LOGIN);
        adminLogEntity.setStatus(IdentityLogStatus.SUCCESS);
        adminLogEntity.setMethod("POST");
        adminLogEntity.setAction("Logout");
        adminLogEntity.setUserAgent(userAgent);
        adminLogEntity.setRemoteAddr(remoteAddr);

        this.adminLogPersistencePort.save(adminLogEntity);
    }
}
