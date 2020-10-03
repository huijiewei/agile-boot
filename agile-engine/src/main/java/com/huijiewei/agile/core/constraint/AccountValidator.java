package com.huijiewei.agile.core.constraint;

import com.huijiewei.agile.core.application.port.inbound.AccountUseCase;
import com.huijiewei.agile.core.application.request.IdentityLoginRequest;
import com.huijiewei.agile.core.consts.AccountType;
import com.huijiewei.agile.core.consts.IdentityLogStatus;
import com.huijiewei.agile.core.consts.IdentityLogType;
import com.huijiewei.agile.core.domain.AbstractIdentityEntity;
import com.huijiewei.agile.core.domain.AbstractIdentityLogEntity;
import com.huijiewei.agile.core.until.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * @author huijiewei
 */
public class AccountValidator implements ConstraintValidator<Account, IdentityLoginRequest> {
    final static int CAPTCHA_RETRY_TIMES = 3;
    private final ApplicationContext applicationContext;
    private AccountUseCase<? extends AbstractIdentityEntity> accountUseCase;
    private String accountTypeMessage;
    private String accountNotExistMessage;
    private String passwordIncorrectMessage;
    private String captchaIncorrectMessage;

    private Boolean captchaEnable;

    private AbstractIdentityEntity identity;

    @Autowired
    public AccountValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(final Account constraintAnnotation) {
        this.accountUseCase = this.applicationContext.getBean(constraintAnnotation.accountService());

        this.accountTypeMessage = constraintAnnotation.accountTypeMessage();
        this.accountNotExistMessage = constraintAnnotation.accountNotExistMessage();
        this.passwordIncorrectMessage = constraintAnnotation.passwordIncorrectMessage();
        this.captchaIncorrectMessage = constraintAnnotation.captchaIncorrectMessage();

        this.captchaEnable = this.accountUseCase.getCaptchaIsEnable();
    }

    private boolean invalidCaptcha(IdentityLoginRequest request, ConstraintValidatorContext context) {
        boolean invalidCaptcha = !this.accountUseCase.verifyCaptcha(
                request.getCaptcha(),
                request.getUserAgent(),
                request.getRemoteAddr()
        );

        if (invalidCaptcha) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.captchaIncorrectMessage)
                    .addPropertyNode("captcha")
                    .addConstraintViolation();
        }

        return invalidCaptcha;
    }

    private boolean validPassword(IdentityLoginRequest request, ConstraintValidatorContext context) {
        String password = request.getPassword();

        String retryKey = "";
        int retryTimes = 0;

        if (this.captchaEnable) {
            retryKey = "IDENTITY" + this.identity.getId();
            retryTimes = this.accountUseCase.getCaptchaRetryTimes(retryKey);

            if (retryTimes >= CAPTCHA_RETRY_TIMES && this.invalidCaptcha(request, context)) {
                return false;
            }
        }

        if (!SecurityUtils.passwordMatches(password, this.identity.getPassword())) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(this.passwordIncorrectMessage)
                    .addPropertyNode("password")
                    .addConstraintViolation();

            if (this.captchaEnable) {
                if (retryTimes >= CAPTCHA_RETRY_TIMES - 1) {
                    context.buildConstraintViolationWithTemplate("")
                            .addPropertyNode("captcha")
                            .addConstraintViolation();
                }

                this.accountUseCase.setCaptchaRetryTimes(retryKey, retryTimes + 1);
            }

            return false;
        }

        if (this.captchaEnable) {
            this.accountUseCase.setCaptchaRetryTimes(retryKey, 0);
        }

        return true;
    }

    private boolean validAccount(IdentityLoginRequest request, ConstraintValidatorContext context) {
        String account = request.getAccount();

        AccountType accountType = new EmailValidator().isValid(account, context) ?
                AccountType.EMAIL :
                (new PhoneValidator().isValid(account, context) ? AccountType.PHONE : null);

        if (accountType == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.accountTypeMessage)
                    .addPropertyNode("account")
                    .addConstraintViolation();

            return false;
        }

        String retryKey = "";

        int retryTimes = 0;

        if (this.captchaEnable) {
            retryKey = SecurityUtils.md5(request.getClientId() + "_" + request.getRemoteAddr()).substring(0, 8);

            retryTimes = this.accountUseCase.getCaptchaRetryTimes(retryKey);

            if (retryTimes >= CAPTCHA_RETRY_TIMES && this.invalidCaptcha(request, context)) {
                return false;
            }
        }

        Optional<? extends AbstractIdentityEntity> identityEntityOptional = this.accountUseCase.getByAccount(account, accountType);

        if (identityEntityOptional.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.accountNotExistMessage)
                    .addPropertyNode("account")
                    .addConstraintViolation();

            if (this.captchaEnable) {
                if (retryTimes >= CAPTCHA_RETRY_TIMES - 1) {
                    context.buildConstraintViolationWithTemplate("")
                            .addPropertyNode("captcha")
                            .addConstraintViolation();
                }

                this.accountUseCase.setCaptchaRetryTimes(retryKey, retryTimes + 1);
            }

            return false;
        }

        this.identity = identityEntityOptional.get();

        if (this.captchaEnable) {
            this.accountUseCase.setCaptchaRetryTimes(retryKey, 0);
        }

        return true;
    }

    @Override
    public boolean isValid(IdentityLoginRequest request, ConstraintValidatorContext context) {
        String account = request.getAccount();
        String password = request.getPassword();

        if (StringUtils.isEmpty(account)) {
            return true;
        }

        if (StringUtils.isEmpty(password)) {
            return true;
        }

        if (!this.validAccount(request, context)) {
            return false;
        }

        AbstractIdentityLogEntity identityLog = this.accountUseCase.createLog(this.identity.getId());

        if (identityLog != null) {
            identityLog.setType(IdentityLogType.LOGIN.getValue());
            identityLog.setMethod("POST");
            identityLog.setAction("Login");
            identityLog.setUserAgent(request.getUserAgent());
            identityLog.setRemoteAddr(request.getRemoteAddr());
        }

        boolean valid = this.validPassword(request, context);

        if (!valid) {
            if (identityLog != null) {
                identityLog.setStatus(IdentityLogStatus.FAIL.getValue());
                identityLog.setException("密码错误");
            }
        } else {
            if (identityLog != null) {
                identityLog.setStatus(IdentityLogStatus.SUCCESS.getValue());
            }
        }

        if (identityLog != null) {
            this.accountUseCase.saveLog(identityLog);
        }

        if (valid) {
            request.setIdentity(this.identity);
        }

        return valid;
    }
}

