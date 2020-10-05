package com.huijiewei.agile.core.consts;

/**
 * @author huijiewei
 */

public class IdentityLogStatus extends ValueDescription<IdentityLogStatus, Integer> {
    public static final IdentityLogStatus FAIL = new IdentityLogStatus(0, "失败");
    public static final IdentityLogStatus SUCCESS = new IdentityLogStatus(1, "成功");

    public IdentityLogStatus(Integer value, String description) {
        super(value, description);
    }

    public static IdentityLogStatus[] values() {
        return values(IdentityLogStatus.class);
    }

    public static IdentityLogStatus valueOf(Integer value) {
        IdentityLogStatus identityLogStatus = valueOf(IdentityLogStatus.class, value);

        if (identityLogStatus == null) {
            identityLogStatus = new IdentityLogStatus(value, value.toString());
        }

        return identityLogStatus;
    }
}
