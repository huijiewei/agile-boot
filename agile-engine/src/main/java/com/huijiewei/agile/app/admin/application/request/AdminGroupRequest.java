package com.huijiewei.agile.app.admin.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author huijiewei
 */

@Getter
@Setter
public class AdminGroupRequest {
    @NotBlank
    @Schema(description = "管理组名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "管理组权限")
    private List<String> permissions;
}
