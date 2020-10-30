package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.admin.application.port.inbound.AdminGroupUseCase;
import com.huijiewei.agile.app.admin.application.request.AdminGroupRequest;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.core.application.response.ListResponse;
import com.huijiewei.agile.core.application.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author huijiewei
 */

@RestController
@Tag(name = "admin-group", description = "管理组")
@RequiredArgsConstructor
public class AdminGroupController {
    private final AdminGroupUseCase adminGroupUseCase;

    @GetMapping(
            value = "/admin-groups",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "管理组列表", operationId = "adminGroupIndex")
    @ApiResponse(responseCode = "200", description = "管理组列表")
    @PreAuthorize("hasAuthority('admin-group/index')")
    public ListResponse<AdminGroupEntity> actionIndex() {
        return this.adminGroupUseCase.all();
    }

    @GetMapping(
            value = "/admin-groups/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "管理组详情", operationId = "adminGroupView")
    @ApiResponse(responseCode = "200", description = "管理组")
    @ApiResponse(responseCode = "404", description = "管理组不存在", ref = "Problem")
    @PreAuthorize("hasAnyAuthority('admin-group/view/:id', 'admin-group/edit/:id')")
    public AdminGroupEntity actionView(@PathVariable("id") Integer id) {
        return this.adminGroupUseCase.read(id);
    }

    @PostMapping(
            value = "/admin-groups",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "管理组新建", operationId = "adminGroupCreate")
    @ApiResponse(responseCode = "201", description = "管理组")
    @ApiResponse(responseCode = "422", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAuthority('admin-group/create')")
    public AdminGroupEntity actionCreate(@RequestBody AdminGroupRequest request) {
        return this.adminGroupUseCase.create(request);
    }

    @PutMapping(
            value = "/admin-groups/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "管理组编辑", operationId = "adminGroupEdit")
    @ApiResponse(responseCode = "200", description = "管理组")
    @ApiResponse(responseCode = "422", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAuthority('admin-group/edit/:id')")
    public AdminGroupEntity actionEdit(@PathVariable("id") Integer id, @RequestBody AdminGroupRequest request) {
        return this.adminGroupUseCase.update(id, request);
    }

    @DeleteMapping(
            value = "/admin-groups/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "管理组删除", operationId = "adminGroupDelete")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", ref = "NotFoundProblem")
    @ApiResponse(responseCode = "409", ref = "ConflictProblem")
    @PreAuthorize("hasAuthority('admin-group/delete')")
    public MessageResponse actionDelete(@PathVariable("id") Integer id) {
        this.adminGroupUseCase.deleteById(id);

        return MessageResponse.of("管理组删除成功");
    }
}
