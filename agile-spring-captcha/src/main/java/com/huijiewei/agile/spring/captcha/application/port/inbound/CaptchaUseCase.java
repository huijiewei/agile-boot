package com.huijiewei.agile.spring.captcha.application.port.inbound;

import com.huijiewei.agile.spring.captcha.application.request.CaptchaRequest;
import com.huijiewei.agile.spring.captcha.application.response.CaptchaResponse;

/**
 * @author huijiewei
 */

public interface CaptchaUseCase {
    Boolean verify(CaptchaRequest request, String userAgent, String remoteAddr);

    CaptchaResponse create(String userAgent, String remoteAddr);
}
