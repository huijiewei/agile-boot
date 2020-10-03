package com.huijiewei.agile.app.admin.application.port.outbound;

import com.huijiewei.agile.app.admin.domain.AdminAccessTokenEntity;

import java.util.Optional;

/**
 * @author huijiewei
 */

public interface AdminAccessTokenPersistencePort {
    Optional<AdminAccessTokenEntity> getByAdminIdAndClientId(Integer adminId, String clientId);

    void delete(Integer identityId, String clientId);

    void save(AdminAccessTokenEntity adminAccessTokenEntity);
}
