package com.huijiewei.agile.core.constraint;

import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huijiewei
 */

public class NotFalseValidator implements ConstraintValidator<NotFalse, Object> {
    private String[] properties;
    private String[] messages;
    private String[] verifiers;

    @Override
    public void initialize(NotFalse flag) {
        properties = flag.properties();
        messages = flag.messages();
        verifiers = flag.verifiers();
    }

    @Override
    public boolean isValid(Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }

        var valid = true;

        var beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);

        for (int i = 0; i < properties.length; i++) {
            var verified = (Boolean) beanWrapper.getPropertyValue(verifiers[i]);
            valid &= isValidProperty(verified, messages[i], properties[i], context);
        }

        return valid;
    }

    boolean isValidProperty(Boolean flag, String message, String property, ConstraintValidatorContext context) {
        if (flag == null || flag) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(property)
                    .addConstraintViolation();
            return false;
        }
    }
}
