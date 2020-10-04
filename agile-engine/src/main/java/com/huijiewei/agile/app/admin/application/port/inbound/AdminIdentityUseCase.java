package com.huijiewei.agile.app.admin.application.port.inbound;

import com.huijiewei.agile.app.admin.application.request.AdminLoginRequest;
import com.huijiewei.agile.app.admin.application.response.AdminIdentityResponse;
import com.huijiewei.agile.app.admin.security.AdminIdentity;

/**
 * @author huijiewei
 */

public interface AdminIdentityUseCase {
    AdminIdentityResponse login(AdminLoginRequest loginRequest);

    AdminIdentityResponse account(AdminIdentity adminIdentity);

    void logout(AdminIdentity adminIdentity, String userAgent, String remoteAddr);
}
