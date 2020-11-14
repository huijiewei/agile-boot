package com.huijiewei.agile.core.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class ValidatingService {
    private final Validator validator;

    public <T> boolean validate(T entity, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity, groups);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return true;
    }
}
