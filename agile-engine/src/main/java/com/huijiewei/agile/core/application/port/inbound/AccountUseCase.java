package com.huijiewei.agile.core.application.port.inbound;

import com.huijiewei.agile.core.consts.AccountType;
import com.huijiewei.agile.core.domain.AbstractIdentityEntity;
import com.huijiewei.agile.core.domain.AbstractIdentityLogEntity;

import java.util.Optional;

/**
 * @author huijiewei
 */

public interface AccountUseCase<T extends AbstractIdentityEntity> {
    Optional<T> getByAccount(String account, AccountType accountType);

    Boolean isCaptchaEnable();

    Boolean verifyCaptcha(String captcha, String userAgent, String remoteAttr);

    Integer getCaptchaRetryTimes(String key);

    void setCaptchaRetryTimes(String key, Integer times);

    <E extends AbstractIdentityLogEntity> E createLog(Integer identityId);

    <E extends AbstractIdentityLogEntity> void saveLog(E log);
}
