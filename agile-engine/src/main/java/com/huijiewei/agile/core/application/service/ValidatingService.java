package com.huijiewei.agile.core.application.service;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author huijiewei
 */

@Service
public class ValidatingService {
    private final Validator validator;

    ValidatingService(Validator validator) {
        this.validator = validator;
    }

    public <T> Boolean validate(T entity, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity, groups);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return true;
    }
}
