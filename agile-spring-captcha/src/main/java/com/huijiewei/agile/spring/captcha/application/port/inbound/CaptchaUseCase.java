package com.huijiewei.agile.spring.captcha.application.port.inbound;

import com.huijiewei.agile.spring.captcha.application.response.CaptchaResponse;

/**
 * @author huijiewei
 */

public interface CaptchaUseCase {
    Boolean verify(String captcha, String userAgent, String remoteAddr);

    CaptchaResponse create(String userAgent, String remoteAddr);
}
