package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.user.application.port.inbound.UserUseCase;
import com.huijiewei.agile.app.user.application.request.UserRequest;
import com.huijiewei.agile.app.user.application.request.UserSearchRequest;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.app.user.domain.UserEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.MessageResponse;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.exception.BadRequestException;
import com.huijiewei.agile.core.until.HttpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author huijiewei
 */

@RestController
@Tag(name = "user", description = "用户接口")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;

    @GetMapping(
            value = "/users",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户列表", operationId = "userIndex")
    @ApiResponse(responseCode = "200", description = "用户列表")
    @PreAuthorize("hasAuthority('user/index')")
    public SearchPageResponse<UserEntity> actionIndex(
            @ParameterObject UserSearchRequest request,
            @ParameterObject Pageable pageable,
            @Parameter(description = "是否返回搜索字段信息") @RequestParam(required = false) Boolean withSearchFields
    ) {
        return this.userUseCase.search(request, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), withSearchFields);
    }

    @GetMapping(
            value = "/users/export",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    @Operation(description = "用户导出", operationId = "userExport")
    @ApiResponse(responseCode = "200", description = "用户导出")
    @PreAuthorize("hasAuthority('user/export')")
    public void actionExport(
            @ParameterObject UserSearchRequest userSearchRequest,
            HttpServletResponse response
    ) {
        try {
            HttpUtils.setExcelDownload("用户列表.xlsx", response);

            this.userUseCase.export(userSearchRequest, response.getOutputStream());

            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception ex) {
            throw new BadRequestException("导出错误:" + ex.getMessage());
        }
    }

    @GetMapping(
            value = "/users/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户详情", operationId = "userView")
    @ApiResponse(responseCode = "200", description = "用户")
    @ApiResponse(responseCode = "404", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('user/view/:id', 'user/edit/:id')")
    public UserEntity actionView(@PathVariable("id") Integer id) {
        return this.userUseCase.loadById(id);
    }

    @PostMapping(
            value = "/users",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户新建", operationId = "userCreate")
    @ApiResponse(responseCode = "201", description = "用户")
    @ApiResponse(responseCode = "422", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('user/create')")
    public UserEntity actionCreate(@RequestBody UserRequest request, HttpServletRequest servletRequest) {
        return this.userUseCase.create(request, UserCreatedFrom.SYSTEM, HttpUtils.getRemoteAddr(servletRequest));
    }

    @PutMapping(
            value = "/users/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户编辑", operationId = "userEdit")
    @ApiResponse(responseCode = "200", description = "用户")
    @ApiResponse(responseCode = "404", ref = "NotFoundProblem")
    @ApiResponse(responseCode = "422", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('user/edit/:id')")
    public UserEntity actionEdit(@PathVariable("id") Integer id, @RequestBody UserRequest request) {
        return this.userUseCase.update(id, request);
    }

    @DeleteMapping(
            value = "/users/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户删除", operationId = "userDelete")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('user/delete')")
    public MessageResponse actionDelete(@PathVariable("id") Integer id) {
        this.userUseCase.deleteById(id);

        return MessageResponse.of("用户删除成功");
    }
}

