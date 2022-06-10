package com.huijiewei.agile.serve.admin.handler;

import com.huijiewei.agile.core.exception.BadRequestException;
import com.huijiewei.agile.core.exception.ConflictException;
import com.huijiewei.agile.core.exception.ForbiddenException;
import com.huijiewei.agile.core.exception.NotFoundException;
import org.apiguardian.api.API;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.zalando.problem.Status.UNPROCESSABLE_ENTITY;

/**
 * @author huijiewei
 */

@API(status = INTERNAL)
@ControllerAdvice
public class ExceptionHandling implements ProblemHandling, SecurityAdviceTrait {
    @Override
    public StatusType defaultConstraintViolationStatus() {
        return UNPROCESSABLE_ENTITY;
    }

    @Override
    public boolean isCausalChainsEnabled() {
        return false;
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUnauthorized(
            final BadCredentialsException exception,
            final NativeWebRequest request) {
        return create(Status.UNAUTHORIZED, exception, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNotFound(
            final NotFoundException exception,
            final NativeWebRequest request) {
        return create(Status.NOT_FOUND, exception, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequest(
            final BadRequestException exception,
            final NativeWebRequest request) {
        return create(Status.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleForbidden(
            final ForbiddenException exception,
            final NativeWebRequest request) {
        return create(Status.FORBIDDEN, exception, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConflict(
            final ConflictException exception,
            final NativeWebRequest request) {
        return create(Status.CONFLICT, exception, request);
    }
}
