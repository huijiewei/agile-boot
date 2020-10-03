package com.huijiewei.agile.app.open.application.port;

import com.huijiewei.agile.app.open.domain.CaptchaEntity;

import java.util.Optional;

/**
 * @author huijiewei
 */

public interface CaptchaPersistencePort {
    Optional<CaptchaEntity> getByCode(String code, String uuid, String userAgent, String remoteAddr);

    void deleteById(Integer id);

    Integer save(CaptchaEntity captchaEntity);
}
