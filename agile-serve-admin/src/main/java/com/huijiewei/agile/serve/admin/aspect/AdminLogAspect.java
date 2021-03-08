package com.huijiewei.agile.serve.admin.aspect;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.consts.IdentityLogStatus;
import com.huijiewei.agile.core.consts.IdentityLogType;
import com.huijiewei.agile.core.until.HttpUtils;
import com.huijiewei.agile.core.until.StringUtils;
import com.huijiewei.agile.serve.admin.security.AdminUserDetails;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author huijiewei
 */

@Aspect
@Component
@RequiredArgsConstructor
public class AdminLogAspect {
    private final AdminLogPersistencePort adminLogPersistencePort;

    @Pointcut("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void preAuthorize() {
    }

    private void setAdminLog(AdminLogEntity adminLogEntity, ProceedingJoinPoint joinPoint) {
        var requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        var queryString = new StringBuilder();

        if (requestAttributes != null) {
            var request = requestAttributes.getRequest();

            var requestMethod = request.getMethod();

            adminLogEntity.setType("GET".equals(requestMethod) ? IdentityLogType.VISIT : IdentityLogType.OPERATE);
            adminLogEntity.setMethod(requestMethod);
            adminLogEntity.setUserAgent(HttpUtils.getUserAgent(request));
            adminLogEntity.setRemoteAddr(HttpUtils.getRemoteAddr(request));

            if (StringUtils.isNotEmpty(request.getQueryString())) {
                queryString.append(request.getQueryString());
            }
        }

        var joinPointClass = joinPoint.getTarget().getClass();
        var joinPointMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();

        adminLogEntity.setAction(joinPointClass.getSimpleName() + "." + joinPointMethod.getName());

        var parameterAnnotations = joinPointMethod.getParameterAnnotations();
        var args = joinPoint.getArgs();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            var parameterAnnotation = parameterAnnotations[i];

            for (var annotation : parameterAnnotation) {
                if (annotation.annotationType().equals(PathVariable.class)) {
                    var pathVariableAnnotation = (PathVariable) annotation;

                    var queryName = pathVariableAnnotation.name();

                    if (StringUtils.isEmpty(queryName)) {
                        queryName = pathVariableAnnotation.value();
                    }

                    if (StringUtils.isNotEmpty(queryString)) {
                        queryString.append("&");
                    }

                    queryString
                            .append(queryName)
                            .append("=")
                            .append(args[i].toString());
                }
            }
        }

        adminLogEntity.setParams(queryString.toString());
    }

    @Around(value = "preAuthorize()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var adminLogEntity = new AdminLogEntity();
        adminLogEntity.setAdminId(AdminUserDetails.getCurrentAdminIdentity().getAdminEntity().getId());

        this.setAdminLog(adminLogEntity, joinPoint);

        try {
            adminLogEntity.setStatus(IdentityLogStatus.SUCCESS);

            return joinPoint.proceed();
        } catch (Exception ex) {
            adminLogEntity.setStatus(IdentityLogStatus.FAIL);
            adminLogEntity.setException(ex.getMessage());

            throw ex;
        } finally {
            this.adminLogPersistencePort.save(adminLogEntity);
        }
    }
}
