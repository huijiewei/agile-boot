package com.huijiewei.agile.spring.captcha.adapter.persistence.mapper;

import com.huijiewei.agile.spring.captcha.adapter.persistence.entity.Captcha;
import com.huijiewei.agile.spring.captcha.domain.CaptchaEntity;
import org.mapstruct.Mapper;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface CaptchaMapper {
    CaptchaEntity toCaptchaEntity(Captcha captcha);

    Captcha toCaptcha(CaptchaEntity captchaEntity);
}

