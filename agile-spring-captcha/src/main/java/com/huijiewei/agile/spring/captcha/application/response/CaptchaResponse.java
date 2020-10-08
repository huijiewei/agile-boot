package com.huijiewei.agile.spring.captcha.application.response;

import lombok.Data;

/**
 * @author huijiewei
 */

@Data
public class CaptchaResponse {
    private String image;

    private String process;
}
