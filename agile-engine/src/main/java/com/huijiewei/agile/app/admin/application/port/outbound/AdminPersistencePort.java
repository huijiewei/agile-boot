package com.huijiewei.agile.app.admin.application.port.outbound;

import com.huijiewei.agile.app.admin.domain.AdminEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface AdminPersistencePort {
    List<AdminEntity> getAll();

    Optional<AdminEntity> getById(Integer id);

    Optional<AdminEntity> getByIdWithAdminGroup(Integer id);

    Integer save(AdminEntity adminEntity);

    void deleteById(Integer id);

    Optional<AdminEntity> getByAccessToken(String accessToken, String clientId);

    Optional<AdminEntity> getByPhone(String phone);

    Optional<AdminEntity> getByEmail(String email);

    boolean existsByAdminGroupId(Integer adminGroupId);
}
