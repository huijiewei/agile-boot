package com.huijiewei.agile.app.user.domain;

import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.core.domain.AbstractIdentityEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class UserEntity extends AbstractIdentityEntity {
    private String name;

    private String avatar;

    private String createdIp;

    private UserCreatedFrom createdFrom;

    private LocalDateTime createdAt;
}
