package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.admin.application.port.inbound.AdminIdentityUseCase;
import com.huijiewei.agile.app.admin.application.request.AdminLoginRequest;
import com.huijiewei.agile.app.admin.application.response.AdminIdentityResponse;
import com.huijiewei.agile.core.application.response.MessageResponse;
import com.huijiewei.agile.core.until.HttpUtils;
import com.huijiewei.agile.serve.admin.security.AdminUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huijiewei
 */

@RestController
@Tag(name = "auth", description = "管理员登录注册")
public class AuthController {
    private final AdminIdentityUseCase adminIdentityUseCase;

    @Autowired
    public AuthController(AdminIdentityUseCase adminIdentityUseCase) {
        this.adminIdentityUseCase = adminIdentityUseCase;
    }

    @PostMapping(
            value = "/auth/login",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "管理员登录", operationId = "authLogin")
    @ApiResponse(responseCode = "200", description = "登录成功")
    @ApiResponse(responseCode = "422", ref = "UnprocessableEntityProblem")
    public AdminIdentityResponse actionLogin(
            @RequestBody AdminLoginRequest request,
            HttpServletRequest servletRequest) {

        request.setClientId(HttpUtils.getClientId(servletRequest));
        request.setUserAgent(HttpUtils.getUserAgent(servletRequest));
        request.setRemoteAddr(HttpUtils.getRemoteAddr(servletRequest));

        return this.adminIdentityUseCase.login(request);
    }

    @Operation(description = "当前登录帐号", operationId = "authAccount")
    @ApiResponse(responseCode = "200", description = "获取成功")
    @GetMapping(
            value = "/auth/account",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public AdminIdentityResponse actionAccount() {
        return this.adminIdentityUseCase.account(AdminUserDetails.getCurrentAdminIdentity());
    }

    @Operation(description = "管理员退出登录", operationId = "authLogout")
    @ApiResponse(responseCode = "200", description = "退出登录成功")
    @PostMapping(
            value = "/auth/logout",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public MessageResponse actionLogout(HttpServletRequest servletRequest) {
        this.adminIdentityUseCase.logout(
                AdminUserDetails.getCurrentAdminIdentity(),
                HttpUtils.getUserAgent(servletRequest),
                HttpUtils.getRemoteAddr(servletRequest));

        return MessageResponse.of("退出登录成功");
    }
}