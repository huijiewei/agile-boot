package com.huijiewei.agile.app.user.domain;

import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.core.domain.AbstractIdentityEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class UserEntity extends AbstractIdentityEntity {
    @Schema(description = "姓名")
    private String name;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "创建 IP")
    private String createdIp;

    @Schema(description = "创建方式")
    private UserCreatedFrom createdFrom;

    @Schema(description = "创建来源")
    private LocalDateTime createdAt;
}
