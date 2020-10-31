package com.huijiewei.agile.core.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author huijiewei
 */

@Documented
@Constraint(validatedBy = NotFalseValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotFalse {
    String message() default "NotFalse";

    String[] messages();

    String[] properties();

    String[] verifiers();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}