package com.huijiewei.agile.core.constraint;

import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

/**
 * @author huijiewei
 */

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String field;
    private String fieldMatch;
    private String message;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        var fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        var fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        boolean valid = Objects.equals(fieldValue, fieldMatchValue);

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(this.message)
                    .addPropertyNode(this.field)
                    .addConstraintViolation();
        }

        return valid;
    }
}
