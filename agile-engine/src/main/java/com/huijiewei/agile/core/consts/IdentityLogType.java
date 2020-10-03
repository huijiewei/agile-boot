package com.huijiewei.agile.core.consts;

/**
 * @author huijiewei
 */

public class IdentityLogType extends ValueDescription<String> {
    public static final IdentityLogType LOGIN = new IdentityLogType("LOGIN", "登录");
    public static final IdentityLogType VISIT = new IdentityLogType("VISIT", "访问");
    public static final IdentityLogType OPERATE = new IdentityLogType("OPERATE", "操作");

    public IdentityLogType(String value, String description) {
        super(value, description);
    }

    public static IdentityLogType[] values() {
        return values(IdentityLogType.class);
    }

    public static IdentityLogType valueOf(String value) {
        IdentityLogType identityLogType = valueOf(IdentityLogType.class, value);

        if (identityLogType == null) {
            identityLogType = new IdentityLogType(value, value);
        }

        return identityLogType;
    }
}
