package com.huijiewei.agile.spring.captcha.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author huijiewei
 */

@Data
public class CaptchaEntity {
    public final static int CAPTCHA_EXPIRED_SECONDS = 60 * 10;

    private Integer id;

    private String uuid;

    private String code;

    private String userAgent;

    private String remoteAddr;

    private LocalDateTime createdAt;

    public Boolean isExpired() {
        return createdAt.plusSeconds(CAPTCHA_EXPIRED_SECONDS).toEpochSecond(ZoneOffset.of("Z")) < LocalDateTime.now().toEpochSecond(ZoneOffset.of("Z"));
    }
}
