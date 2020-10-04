package com.huijiewei.agile.app.admin.adapter.persistence;

import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminAccessTokenMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.JpaAdminAccessTokenRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminAccessTokenPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminAccessTokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
class JpaAdminAccessTokenAdapter implements AdminAccessTokenPersistencePort {
    private final AdminAccessTokenMapper adminAccessTokenMapper;
    private final JpaAdminAccessTokenRepository jpaAdminAccessTokenRepository;

    public JpaAdminAccessTokenAdapter(AdminAccessTokenMapper adminAccessTokenMapper, JpaAdminAccessTokenRepository jpaAdminAccessTokenRepository) {
        this.adminAccessTokenMapper = adminAccessTokenMapper;
        this.jpaAdminAccessTokenRepository = jpaAdminAccessTokenRepository;
    }

    @Override
    public Optional<AdminAccessTokenEntity> getByAdminIdAndClientId(Integer adminId, String clientId) {
        return this.jpaAdminAccessTokenRepository
                .findByAdminIdAndClientId(adminId, clientId)
                .map(this.adminAccessTokenMapper::toAdminAccessTokenEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer identityId, String clientId) {
        this.jpaAdminAccessTokenRepository.deleteByAdminIdAndClientId(identityId, clientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AdminAccessTokenEntity adminAccessTokenEntity) {
        this.jpaAdminAccessTokenRepository.save(this.adminAccessTokenMapper.toAdminAccessToken(adminAccessTokenEntity));
    }
}
