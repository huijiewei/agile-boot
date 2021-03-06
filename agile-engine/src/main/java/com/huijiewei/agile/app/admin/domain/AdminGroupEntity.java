package com.huijiewei.agile.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huijiewei.agile.app.admin.application.service.AdminGroupUniqueService;
import com.huijiewei.agile.core.constraint.Unique;
import com.huijiewei.agile.core.domain.AbstractEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Unique(fields = "name", uniqueService = AdminGroupUniqueService.class, message = "名称已被使用")
public class AdminGroupEntity extends AbstractEntity {
    @Schema(description = "管理组名称")
    private String name;

    @Schema(description = "管理组权限")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> permissions = new ArrayList<>();
}
