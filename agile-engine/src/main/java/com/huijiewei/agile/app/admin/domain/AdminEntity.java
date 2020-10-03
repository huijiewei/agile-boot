package com.huijiewei.agile.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class AdminEntity extends AbstractIdentityEntity {
    @Schema(description = "姓名")
    private String name;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "所属管理组 Id")
    private Integer adminGroupId;

    @Schema(description = "所属管理组")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AdminGroupEntity adminGroup;
}
