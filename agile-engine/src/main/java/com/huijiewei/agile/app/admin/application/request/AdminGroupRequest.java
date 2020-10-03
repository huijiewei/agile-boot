package com.huijiewei.agile.app.admin.application.request;

import com.huijiewei.agile.app.admin.application.service.AdminGroupUniqueService;
import com.huijiewei.agile.core.constraint.Unique;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author huijiewei
 */

@Data
@Unique(fields = "name", uniqueService = AdminGroupUniqueService.class, message = "名称已被使用")
public class AdminGroupRequest {
    private Integer id;

    @NotBlank
    @Schema(description = "管理组名称", required = true)
    private String name;

    @Schema(description = "管理组权限")
    private List<String> permissions;
}
