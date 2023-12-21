package com.huijiewei.agile.core.constraint;

import com.huijiewei.agile.core.application.port.inbound.AccountUseCase;
import com.huijiewei.agile.core.application.request.AbstractIdentityLoginRequest;
import com.huijiewei.agile.core.consts.AccountType;
import com.huijiewei.agile.core.consts.IdentityLogStatus;
import com.huijiewei.agile.core.consts.IdentityLogType;
import com.huijiewei.agile.core.domain.AbstractIdentityEntity;
import com.huijiewei.agile.core.until.SecurityUtils;
import com.huijiewei.agile.core.until.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author huijiewei
 */
public class AccountValidator implements ConstraintValidator<Account, AbstractIdentityLoginRequest> {
    final static int RETRY_TIMES = 3;

    private final ApplicationContext applicationContext;
    private AccountUseCase<? extends AbstractIdentityEntity> accountUseCase;
    private String accountTypeMessage;
    private String accountNotExistMessage;
    private String passwordIncorrectMessage;
    private String captchaIncorrectMessage;
    private String captchaRequiredMessage;

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
        this.captchaRequiredMessage = constraintAnnotation.captchaRequiredMessage();
    }

    private boolean invalidCaptcha(AbstractIdentityLoginRequest request, ConstraintValidatorContext context) {
        boolean validCaptcha = this.accountUseCase.verifyCaptcha(
                request.getCaptcha(),
                request.getUserAgent(),
                request.getRemoteAddr()
        );

        if (!validCaptcha) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.captchaIncorrectMessage)
                    .addPropertyNode("captcha")
                    .addConstraintViolation();

            return true;
        }

        return false;
    }

    private boolean validPassword(AbstractIdentityLoginRequest request, ConstraintValidatorContext context) {
        var password = request.getPassword();

        var retryTimesCacheKey = SecurityUtils.md5(request.getAccount()).substring(0, 8);

        var retryTimes = this.accountUseCase.getRetryTimes(retryTimesCacheKey);

        if (retryTimes > RETRY_TIMES && this.invalidCaptcha(request, context)) {
            return false;
        }

        if (!SecurityUtils.passwordMatches(password, request.getIdentity().getPassword())) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(this.passwordIncorrectMessage)
                    .addPropertyNode("password")
                    .addConstraintViolation();

            if (retryTimes >= RETRY_TIMES) {
                context.buildConstraintViolationWithTemplate(this.captchaRequiredMessage)
                        .addPropertyNode("captcha")
                        .addConstraintViolation();
            }

            this.accountUseCase.setRetryTimes(retryTimesCacheKey, retryTimes + 1);

            return false;
        }

        this.accountUseCase.setRetryTimes(retryTimesCacheKey, 0);

        return true;
    }

    private boolean validAccount(AbstractIdentityLoginRequest request, ConstraintValidatorContext context) {
        var account = request.getAccount();

        var accountType = new EmailValidator().isValid(account, context) ?
                AccountType.EMAIL :
                (new PhoneValidator().isValid(account, context) ? AccountType.PHONE : null);

        if (accountType == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.accountTypeMessage)
                    .addPropertyNode("account")
                    .addConstraintViolation();

            return false;
        }

        var retryTimesCacheKey = SecurityUtils.md5(request.getClientId() + request.getRemoteAddr()).substring(0, 8);
        var retryTimes = this.accountUseCase.getRetryTimes(retryTimesCacheKey);

        if (retryTimes > RETRY_TIMES && this.invalidCaptcha(request, context)) {
            return false;
        }

        var identityEntityOptional = this.accountUseCase.getByAccount(account, accountType);

        if (identityEntityOptional.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.accountNotExistMessage)
                    .addPropertyNode("account")
                    .addConstraintViolation();

            if (retryTimes >= RETRY_TIMES) {
                context.buildConstraintViolationWithTemplate(this.captchaRequiredMessage)
                        .addPropertyNode("captcha")
                        .addConstraintViolation();
            }

            this.accountUseCase.setRetryTimes(retryTimesCacheKey, retryTimes + 1);

            return false;
        }

        request.setIdentity(identityEntityOptional.get());

        this.accountUseCase.setRetryTimes(retryTimesCacheKey, 0);

        return true;
    }

    @Override
    public boolean isValid(AbstractIdentityLoginRequest request, ConstraintValidatorContext context) {
        var account = request.getAccount();
        var password = request.getPassword();

        if (StringUtils.isEmpty(account)) {
            return true;
        }

        if (StringUtils.isEmpty(password)) {
            return true;
        }

        if (!this.validAccount(request, context)) {
            return false;
        }

        var identityLog = this.accountUseCase.createLog(request.getIdentity().getId());

        if (identityLog != null) {
            identityLog.setType(IdentityLogType.LOGIN);
            identityLog.setMethod("POST");
            identityLog.setAction("Login");
            identityLog.setUserAgent(request.getUserAgent());
            identityLog.setRemoteAddr(request.getRemoteAddr());
        }

        var valid = this.validPassword(request, context);

        if (!valid) {
            if (identityLog != null) {
                identityLog.setStatus(IdentityLogStatus.FAIL);
                identityLog.setException("密码错误");
            }
        } else {
            if (identityLog != null) {
                identityLog.setStatus(IdentityLogStatus.SUCCESS);
            }
        }

        if (identityLog != null) {
            this.accountUseCase.saveLog(identityLog);
        }

        return valid;
    }
}

