package com.huijiewei.agile.app.user.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huijiewei.agile.app.user.application.service.UserUniqueService;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.core.constraint.Unique;
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
@Unique.List({
        @Unique(fields = "phone", uniqueService = UserUniqueService.class, message = "手机号码已被使用"),
        @Unique(fields = "email", uniqueService = UserUniqueService.class, message = "电子邮箱已被使用")
})
public class UserEntity extends AbstractIdentityEntity {
    @Schema(description = "姓名")
    private String name;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "创建 IP")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdIp;

    @Schema(description = "创建来源")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserCreatedFrom createdFrom;

    @Schema(description = "创建时间")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdAt;
}
