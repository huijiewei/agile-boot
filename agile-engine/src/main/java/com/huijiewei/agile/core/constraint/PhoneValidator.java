package com.huijiewei.agile.core.constraint;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huijiewei
 */

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    @Override
    public boolean isValid(final String phone, final ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(phone)) {
            return true;
        }

        return phone.matches("^1([34578])\\d{9}$");
    }
}