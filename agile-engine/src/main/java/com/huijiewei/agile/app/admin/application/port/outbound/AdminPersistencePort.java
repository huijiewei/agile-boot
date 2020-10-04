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

    /**
     * 根据 AccessToken 和 clientId 查找用户
     *
     * @param accessToken accessToken
     * @param clientId    clientId
     * @return 用户
     */
    Optional<AdminEntity> getByAccessToken(String accessToken, String clientId);

    /**
     * 根据 phone 查找用户
     *
     * @param phone phone
     * @return 用户
     */
    Optional<AdminEntity> getByPhone(String phone);

    /**
     * 根据 email 查找用户
     *
     * @param email email
     * @return 用户
     */
    Optional<AdminEntity> getByEmail(String email);

    Boolean existsByAdminGroupId(Integer adminGroupId);
}
