package com.huijiewei.agile.serve.admin.aspect;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.consts.IdentityLogStatus;
import com.huijiewei.agile.core.consts.IdentityLogType;
import com.huijiewei.agile.core.until.HttpUtils;
import com.huijiewei.agile.serve.admin.security.AdminUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author huijiewei
 */

@Aspect
@Component
public class AdminLogAspect {
    private final AdminLogPersistencePort adminLogPersistencePort;

    public AdminLogAspect(AdminLogPersistencePort adminLogPersistencePort) {
        this.adminLogPersistencePort = adminLogPersistencePort;
    }

    @Pointcut("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void preAuthorize() {
    }

    private void setAdminLog(AdminLogEntity adminLogEntity, ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        StringBuilder queryString = new StringBuilder();

        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();

            String requestMethod = request.getMethod();

            adminLogEntity.setType("GET".equals(requestMethod) ? IdentityLogType.VISIT : IdentityLogType.OPERATE);
            adminLogEntity.setMethod(requestMethod);
            adminLogEntity.setUserAgent(HttpUtils.getUserAgent(request));
            adminLogEntity.setRemoteAddr(HttpUtils.getRemoteAddr(request));

            if (StringUtils.isNotEmpty(request.getQueryString())) {
                queryString.append(request.getQueryString());
            }
        }

        Class<?> joinPointClass = joinPoint.getTarget().getClass();
        Method joinPointMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();

        adminLogEntity.setAction(joinPointClass.getSimpleName() + "." + joinPointMethod.getName());

        Object[][] parameterAnnotations = joinPointMethod.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            Object[] parameterAnnotation = parameterAnnotations[i];

            for (Object object : parameterAnnotation) {
                Annotation annotation = (Annotation) object;

                if (annotation.annotationType().equals(PathVariable.class)) {
                    PathVariable pathVariableAnnotation = (PathVariable) annotation;

                    String queryName = pathVariableAnnotation.name();

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
        AdminLogEntity adminLogEntity = new AdminLogEntity();
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
