package com.huijiewei.agile.core.constraint;

import com.huijiewei.agile.core.application.port.inbound.AccountUseCase;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huijiewei
 */

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountValidator.class)
public @interface Account {
    String message() default "";

    Class<? extends AccountUseCase<?>> accountService();

    String accountTypeMessage() default "Invalid account type, account should be the phone or email";

    String accountNotExistMessage() default "Account is not exists";

    String passwordIncorrectMessage() default "Password is incorrect";

    String captchaIncorrectMessage() default "Captcha is incorrect";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
