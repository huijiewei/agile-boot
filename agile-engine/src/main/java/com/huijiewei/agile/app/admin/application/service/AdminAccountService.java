package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.app.open.application.port.CaptchaPersistencePort;
import com.huijiewei.agile.app.open.domain.CaptchaEntity;
import com.huijiewei.agile.core.application.port.inbound.AccountUseCase;
import com.huijiewei.agile.core.consts.AccountType;
import com.huijiewei.agile.core.domain.AbstractIdentityLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author huijiewei
 */

@Service
public class AdminAccountService implements AccountUseCase<AdminEntity> {
    final static String CAPTCHA_SPLIT_CHAR = "_";
    final static Integer CAPTCHA_SPLIT_LENGTH = 2;
    final static String CAPTCHA_CACHE_NAME = "admin-account-captcha-cache";

    private final AdminPersistencePort adminPersistencePort;
    private final AdminLogPersistencePort adminLogPersistencePort;
    private final CaptchaPersistencePort captchaPersistencePort;
    private final ConcurrentMapCacheManager concurrentMapCacheManager;

    public AdminAccountService(AdminPersistencePort adminPersistencePort, AdminLogPersistencePort adminLogPersistencePort, CaptchaPersistencePort captchaPersistencePort, ConcurrentMapCacheManager concurrentMapCacheManager) {
        this.adminPersistencePort = adminPersistencePort;
        this.adminLogPersistencePort = adminLogPersistencePort;
        this.captchaPersistencePort = captchaPersistencePort;
        this.concurrentMapCacheManager = concurrentMapCacheManager;
    }

    @Override
    public Optional<AdminEntity> getByAccount(String account, AccountType accountType) {
        if (accountType == AccountType.EMAIL) {
            return this.adminPersistencePort.getByEmail(account);
        }

        if (accountType == AccountType.PHONE) {
            return this.adminPersistencePort.getByPhone(account);
        }

        return Optional.empty();
    }

    @Override
    public Boolean getCaptchaIsEnable() {
        return true;
    }

    @Override
    public Boolean verifyCaptcha(String captcha, String userAgent, String remoteAttr) {
        if (StringUtils.isEmpty(captcha)) {
            return false;
        }

        String[] captchaSplit = captcha.split(CAPTCHA_SPLIT_CHAR);

        if (captchaSplit.length != CAPTCHA_SPLIT_LENGTH) {
            return false;
        }

        Optional<CaptchaEntity> captchaEntityOptional = this.captchaPersistencePort.getByCode(
                captchaSplit[0],
                captchaSplit[1],
                userAgent,
                remoteAttr
        );

        if (captchaEntityOptional.isEmpty()) {
            return false;
        }

        this.captchaPersistencePort.deleteById(captchaEntityOptional.get().getId());

        return true;
    }

    @Override
    public Integer getCaptchaRetryTimes(String key) {
        Integer times = Objects.requireNonNull(this.concurrentMapCacheManager.getCache(CAPTCHA_CACHE_NAME)).get(key, Integer.class);

        if (times == null) {
            return 0;
        }

        return times;
    }

    @Override
    public void setCaptchaRetryTimes(String key, Integer times) {
        Objects.requireNonNull(this.concurrentMapCacheManager.getCache(CAPTCHA_CACHE_NAME)).put(key, times);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends AbstractIdentityLogEntity> E createLog(Integer identityId) {
        AdminLogEntity adminLogEntity = new AdminLogEntity();
        adminLogEntity.setAdminId(identityId);

        return (E) adminLogEntity;
    }

    @Override
    public <E extends AbstractIdentityLogEntity> void saveLog(E log) {
        this.adminLogPersistencePort.save((AdminLogEntity) log);
    }
}
