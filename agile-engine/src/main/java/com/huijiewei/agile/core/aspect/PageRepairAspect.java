package com.huijiewei.agile.core.aspect;

import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.PageResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author huijiewei
 */

@Aspect
@Component
public class PageRepairAspect {
    @Pointcut("!@annotation(com.huijiewei.agile.core.aspect.PageRepairAspect.DisablePageRepair) " +
            "&& @target(org.springframework.stereotype.Service) " +
            "&& execution(public com.huijiewei.agile.core.application.response.PageResponse+ *(..))")
    public void methodPointcut() {
    }

    @Around(value = "methodPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var result = joinPoint.proceed();

        if (!(result instanceof PageResponse)) {
            return result;
        }

        var pageResponse = (PageResponse<?>) result;

        if (pageResponse.getPages() == null) {
            return result;
        }

        if (pageResponse.getPages().getPageCount() < 1) {
            return result;
        }

        if (pageResponse.getPages().getCurrentPage() <= pageResponse.getPages().getPageCount()) {
            return result;
        }

        var args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            var arg = args[i];
            if (arg instanceof PageRequest) {
                var page = (PageRequest) arg;
                args[i] = PageRequest.of(pageResponse.getPages().getPageCount() - 1, page.getSize());
            }
        }

        return joinPoint.proceed(args);
    }

    /**
     * 禁用自动修复页码功能
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface DisablePageRepair {
    }
}
