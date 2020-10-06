package com.huijiewei.agile.spring.captcha.application.request;

import lombok.Data;

/**
 * @author huijiewei
 */

@Data
public class CaptchaRequest {
    private String uuid;

    private String code;
}
