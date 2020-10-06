package com.huijiewei.agile.spring.captcha;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author huijiewei
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = CaptchaProperties.PREFIX)
public class CaptchaProperties {
    public static final String PREFIX = "agile.spring.captcha";

    private Integer width = 100;
    private Integer height = 20;
    private Integer length = 5;
}
