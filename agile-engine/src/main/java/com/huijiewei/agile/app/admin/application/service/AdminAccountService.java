package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.application.service.AbstractAccountService;
import com.huijiewei.agile.core.consts.AccountType;
import com.huijiewei.agile.core.domain.AbstractIdentityLogEntity;
import com.huijiewei.agile.spring.captcha.application.port.inbound.CaptchaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class AdminAccountService extends AbstractAccountService<AdminEntity> {
    final static String RETRY_TIMES_CACHE_NAME = "admin-account-retry-times-cache";

    private final AdminPersistencePort adminPersistencePort;
    private final AdminLogPersistencePort adminLogPersistencePort;
    private final CaptchaUseCase captchaUseCase;

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
    protected Boolean isCaptchaEnable() {
        return true;
    }

    @Override
    protected boolean verifyCaptchaImpl(String captcha, String userAgent, String remoteAttr) {
        return this.captchaUseCase.verify(captcha, userAgent, remoteAttr);
    }

    @Override
    protected String getRetryTimesCacheName() {
        return RETRY_TIMES_CACHE_NAME;
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
