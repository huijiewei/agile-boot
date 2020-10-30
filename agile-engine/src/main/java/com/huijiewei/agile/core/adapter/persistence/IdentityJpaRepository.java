package com.huijiewei.agile.core.adapter.persistence;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * @author huijiewei
 */

@NoRepositoryBean
public interface IdentityJpaRepository<T extends AbstractJpaIdentityEntity> {
    /**
     * 手机号查询
     *
     * @param phone 手机号
     * @return 用户
     */
    Optional<T> findByPhone(String phone);

    /**
     * 邮箱查询
     *
     * @param email 邮箱
     * @return 用户
     */
    Optional<T> findByEmail(String email);

    /**
     * 根据 accessToken 和 clientId 获取用户
     *
     * @param accessToken accessToken
     * @param clientId    clientId
     * @return 用户
     */
    Optional<T> findByAccessToken(String accessToken, String clientId);
}
