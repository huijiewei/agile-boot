package com.huijiewei.agile.app.open.adapter.persistence.mapper;

import com.huijiewei.agile.app.open.adapter.persistence.entity.Captcha;
import com.huijiewei.agile.app.open.domain.CaptchaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaptchaMapper {
    CaptchaEntity toCaptchaEntity(Captcha captcha);

    Captcha toCaptcha(CaptchaEntity captchaEntity);
}

