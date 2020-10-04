package com.huijiewei.agile.app.admin.application.request;

import com.huijiewei.agile.app.admin.application.service.AdminGroupUniqueService;
import com.huijiewei.agile.core.constraint.Unique;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author huijiewei
 */

@Getter
@Setter
public class AdminGroupRequest {
    @NotBlank
    @Schema(description = "管理组名称", required = true)
    private String name;

    @Schema(description = "管理组权限")
    private List<String> permissions;
}
