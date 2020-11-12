package com.huijiewei.agile.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huijiewei.agile.app.admin.application.service.AdminUniqueService;
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
        @Unique(fields = "phone", uniqueService = AdminUniqueService.class, message = "手机号码已被使用"),
        @Unique(fields = "email", uniqueService = AdminUniqueService.class, message = "电子邮箱已被使用")
})
public class AdminEntity extends AbstractIdentityEntity {
    @Schema(description = "姓名")
    private String name;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "创建时间")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdAt;

    @Schema(description = "所属管理组 Id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer adminGroupId;

    @Schema(description = "所属管理组")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AdminGroupEntity adminGroup;
}
