package com.huijiewei.agile.core.application.service;

import com.huijiewei.agile.core.application.port.inbound.AccountUseCase;
import com.huijiewei.agile.core.domain.AbstractIdentityEntity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huijiewei
 */

public abstract class AbstractAccountService<T extends AbstractIdentityEntity> implements AccountUseCase<T> {
    private static final ConcurrentHashMap<String, Integer> TIMES_CACHE_MAP = new ConcurrentHashMap<>();

    protected abstract boolean isCaptchaDisabled();

    protected abstract String getRetryTimesCacheName();

    private String getCacheKey(String key) {
        return this.getRetryTimesCacheName() + key;
    }

    protected abstract boolean verifyCaptchaImpl(String captcha, String userAgent, String remoteAttr);

    public boolean verifyCaptcha(String captcha, String userAgent, String remoteAttr) {
        if (this.isCaptchaDisabled()) {
            return true;
        }

        return this.verifyCaptchaImpl(captcha, userAgent, remoteAttr);
    }

    public Integer getRetryTimes(String key) {
        if (this.isCaptchaDisabled()) {
            return 0;
        }

        Integer times = TIMES_CACHE_MAP.get(this.getCacheKey(key));

        if (times == null) {
            return 0;
        }

        return times;
    }

    public void setRetryTimes(String key, Integer times) {
        if (this.isCaptchaDisabled()) {
            return;
        }

        if (times == 0) {
            TIMES_CACHE_MAP.remove(this.getCacheKey(key));
        } else {
            TIMES_CACHE_MAP.put(this.getCacheKey(key), times);
        }
    }
}
