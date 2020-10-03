package com.huijiewei.agile.app.admin.application.response;

import com.huijiewei.agile.app.admin.domain.AdminEntity;
import com.huijiewei.agile.app.admin.security.AdminGroupMenuItem;
import com.huijiewei.agile.core.application.response.IdentityResponse;
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
public class AdminIdentityResponse extends IdentityResponse<AdminEntity> {
    @Schema(description = "菜单列表")
    private List<AdminGroupMenuItem> groupMenus = new ArrayList<>();

    @Schema(description = "权限列表")
    private List<String> groupPermissions = new ArrayList<>();
}
