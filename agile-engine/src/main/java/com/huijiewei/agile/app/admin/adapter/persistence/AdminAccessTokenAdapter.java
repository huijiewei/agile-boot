package com.huijiewei.agile.app.admin.adapter.persistence;

import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminAccessTokenMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.AdminAccessTokenRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminAccessTokenPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminAccessTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
class AdminAccessTokenAdapter implements AdminAccessTokenPersistencePort {
    private final AdminAccessTokenMapper adminAccessTokenMapper;
    private final AdminAccessTokenRepository adminAccessTokenRepository;

    @Override
    public Optional<AdminAccessTokenEntity> getByAdminIdAndClientId(Integer adminId, String clientId) {
        return this.adminAccessTokenRepository
                .findByAdminIdAndClientId(adminId, clientId)
                .map(this.adminAccessTokenMapper::toAdminAccessTokenEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer identityId, String clientId) {
        this.adminAccessTokenRepository.deleteByAdminIdAndClientId(identityId, clientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AdminAccessTokenEntity adminAccessTokenEntity) {
        this.adminAccessTokenRepository.save(this.adminAccessTokenMapper.toAdminAccessToken(adminAccessTokenEntity));
    }
}
