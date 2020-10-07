package com.huijiewei.agile.core.application.request;

import com.huijiewei.agile.core.domain.AbstractIdentityEntity;
import com.huijiewei.agile.spring.captcha.application.request.CaptchaRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author huijiewei
 */

@Getter
@Setter
public abstract class AbstractIdentityLoginRequest {
    @NotBlank(message = "帐号不能为空")
    @Schema(description = "帐号，手机号码或者电子邮箱", required = true)
    private String account;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", required = true)
    private String password;

    @Schema(description = "验证码", nullable = true)
    private CaptchaRequest captcha;

    @Schema(hidden = true)
    private String clientId;

    @Schema(hidden = true)
    private String userAgent;

    @Schema(hidden = true)
    private String remoteAddr;

    @Schema(hidden = true)
    private AbstractIdentityEntity identity;
}
