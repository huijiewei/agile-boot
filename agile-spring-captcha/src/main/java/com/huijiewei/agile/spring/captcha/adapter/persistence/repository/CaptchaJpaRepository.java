package com.huijiewei.agile.spring.captcha.adapter.persistence.repository;

import com.huijiewei.agile.spring.captcha.adapter.persistence.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author huijiewei
 */

@Repository
public interface CaptchaJpaRepository extends JpaRepository<Captcha, Integer> {
    /**
     * 获取验证码
     *
     * @param code       验证码
     * @param uuid       标识
     * @param userAgent  浏览器
     * @param remoteAddr IP 地址
     * @return 返回验证码
     */
    Optional<Captcha> findByCodeAndUuidAndUserAgentAndRemoteAddr(String code, String uuid, String userAgent, String remoteAddr);
}
