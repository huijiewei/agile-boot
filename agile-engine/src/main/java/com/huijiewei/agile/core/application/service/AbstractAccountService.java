package com.huijiewei.agile.core.application.service;

import com.huijiewei.agile.core.application.port.inbound.AccountUseCase;
import com.huijiewei.agile.core.domain.AbstractIdentityEntity;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.Objects;

/**
 * @author huijiewei
 */

public abstract class AbstractAccountService<T extends AbstractIdentityEntity> implements AccountUseCase<T> {
    private final ConcurrentMapCacheManager concurrentMapCacheManager;

    protected AbstractAccountService(ConcurrentMapCacheManager concurrentMapCacheManager) {
        this.concurrentMapCacheManager = concurrentMapCacheManager;
    }

    protected abstract Boolean isCaptchaEnable();

    protected abstract String getRetryTimesCacheName();

    protected abstract Boolean verifyCaptchaImpl(String captcha, String userAgent, String remoteAttr);

    public Boolean verifyCaptcha(String captcha, String userAgent, String remoteAttr) {
        if (!this.isCaptchaEnable()) {
            return true;
        }

        return this.verifyCaptchaImpl(captcha, userAgent, remoteAttr);
    }

    public Integer getRetryTimes(String key) {
        if (!this.isCaptchaEnable()) {
            return 0;
        }

        Integer times = Objects.requireNonNull(this.concurrentMapCacheManager.getCache(this.getRetryTimesCacheName())).get(key, Integer.class);

        if (times == null) {
            return 0;
        }

        return times;
    }

    public void setRetryTimes(String key, Integer times) {
        if (!this.isCaptchaEnable()) {
            return;
        }

        Objects.requireNonNull(this.concurrentMapCacheManager.getCache(this.getRetryTimesCacheName())).put(key, times);
    }
}
