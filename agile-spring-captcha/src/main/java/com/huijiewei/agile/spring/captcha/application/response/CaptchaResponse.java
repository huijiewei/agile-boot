package com.huijiewei.agile.spring.captcha.application.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author huijiewei
 */

@Data
public class CaptchaResponse {
    private String uuid;

    private String captcha;

    private LocalDateTime expiredAt;
}
