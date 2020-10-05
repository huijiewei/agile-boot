package com.huijiewei.agile.app.user.consts;

import com.huijiewei.agile.core.consts.ValueDescription;

/**
 * @author huijiewei
 */

public class UserCreatedFrom extends ValueDescription<UserCreatedFrom, String> {
    public static final UserCreatedFrom APP = new UserCreatedFrom("APP", "应用");
    public static final UserCreatedFrom WEB = new UserCreatedFrom("WEB", "网页");
    public static final UserCreatedFrom WECHAT = new UserCreatedFrom("WECHAT", "微信");
    public static final UserCreatedFrom SYSTEM = new UserCreatedFrom("SYSTEM", "系统");

    public UserCreatedFrom(String value, String description) {
        super(value, description);
    }

    public static UserCreatedFrom[] values() {
        return values(UserCreatedFrom.class);
    }

    public static UserCreatedFrom valueOf(String value) {
        UserCreatedFrom userCreatedFrom = valueOf(UserCreatedFrom.class, value);

        if (userCreatedFrom == null) {
            userCreatedFrom = new UserCreatedFrom(value, value);
        }

        return userCreatedFrom;
    }
}
