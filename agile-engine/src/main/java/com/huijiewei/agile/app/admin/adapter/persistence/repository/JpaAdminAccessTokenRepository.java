package com.huijiewei.agile.app.admin.adapter.persistence.repository;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author huijiewei
 */

@Repository
public interface JpaAdminAccessTokenRepository extends
        CrudRepository<AdminAccessToken, Integer> {
    Optional<AdminAccessToken> findByAdminIdAndClientId(Integer adminId, String clientId);

    void deleteByAdminIdAndClientId(Integer adminId, String clientId);
}
