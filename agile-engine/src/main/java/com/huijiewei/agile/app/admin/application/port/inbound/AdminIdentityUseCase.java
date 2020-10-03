package com.huijiewei.agile.app.admin.application.port.inbound;

import com.huijiewei.agile.app.admin.application.request.AdminLoginRequest;
import com.huijiewei.agile.app.admin.application.response.AdminIdentityResponse;
import com.huijiewei.agile.app.admin.security.AdminIdentity;

import javax.validation.Valid;

/**
 * @author huijiewei
 */

public interface AdminIdentityUseCase {
    public AdminIdentityResponse login(@Valid AdminLoginRequest loginRequest);

    public AdminIdentityResponse account(AdminIdentity adminIdentity);

    public void logout(AdminIdentity adminIdentity, String userAgent, String remoteAddr);
}
