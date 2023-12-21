package com.huijiewei.agile.core.constraint;

import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author huijiewei
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsValidator.class)
@Documented
public @interface Exists {
    Class<? extends ExistsUseCase> existService();

    String targetProperty();

    String[] allowValues() default {};

    String message() default "Such %s not exists!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
