package com.huijiewei.agile.app.admin.security;

import com.huijiewei.agile.app.admin.domain.AdminEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author huijiewei
 */

@Getter
@Setter
public class AdminIdentity {
    private String clientId;

    private AdminEntity adminEntity;
}
