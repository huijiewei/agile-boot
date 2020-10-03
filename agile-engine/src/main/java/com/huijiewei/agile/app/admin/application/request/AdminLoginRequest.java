package com.huijiewei.agile.app.admin.application.request;

import com.huijiewei.agile.app.admin.application.service.AdminAccountService;
import com.huijiewei.agile.core.application.request.IdentityLoginRequest;
import com.huijiewei.agile.core.constraint.Account;
import lombok.Getter;
import lombok.Setter;

/**
 * @author huijiewei
 */

@Getter
@Setter
@Account(
        accountService = AdminAccountService.class,
        accountTypeMessage = "无效的帐号类型, 帐号应该是手机号码或者电子邮箱",
        accountNotExistMessage = "帐号不存在",
        passwordIncorrectMessage = "密码错误",
        captchaIncorrectMessage = "验证码错误")
public class AdminLoginRequest extends IdentityLoginRequest {
}

