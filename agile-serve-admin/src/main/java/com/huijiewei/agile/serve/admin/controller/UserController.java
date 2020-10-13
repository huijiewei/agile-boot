package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.user.application.port.inbound.UserUseCase;
import com.huijiewei.agile.app.user.application.request.UserRequest;
import com.huijiewei.agile.app.user.application.request.UserSearchRequest;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.app.user.domain.UserEntity;
import com.huijiewei.agile.core.application.response.MessageResponse;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.exception.BadRequestException;
import com.huijiewei.agile.core.until.HttpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author huijiewei
 */

@RestController
@Tag(name = "user", description = "用户接口")
public class UserController {
    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping(
            value = "/users",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户列表", operationId = "userIndex", parameters = {
            @Parameter(name = "name", description = "名称", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "phone", description = "手机号码", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "email", description = "电子邮箱", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "page", description = "分页页码", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "size", description = "分页大小", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "createdFrom", description = "创建来源", in = ParameterIn.QUERY, schema = @Schema(ref = "UserCreatedFromSearchRequestSchema")),
            @Parameter(name = "createdRange", description = "创建日期区间", in = ParameterIn.QUERY, schema = @Schema(ref = "DateRangeSearchRequestSchema"))
    })
    @ApiResponse(responseCode = "200", description = "用户列表")
    @PreAuthorize("hasAuthority('user/index')")
    public SearchPageResponse<UserEntity> actionIndex(
            @Parameter(description = "是否返回搜索字段信息") @RequestParam(required = false) Boolean withSearchFields,
            @Parameter(hidden = true) UserSearchRequest request,
            @Parameter(hidden = true) Pageable pageable
    ) {
        return this.userUseCase.all(pageable.getPageNumber(), pageable.getPageSize(), request, withSearchFields);
    }

    @GetMapping(
            value = "/users/export",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    @Operation(description = "用户导出", operationId = "userExport", parameters = {
            @Parameter(name = "name", description = "名称", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "phone", description = "手机号码", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "email", description = "电子邮箱", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "createdFrom", description = "创建来源", in = ParameterIn.QUERY, schema = @Schema(ref = "UserCreatedFromSearchRequestSchema")),
            @Parameter(name = "createdRange", description = "创建日期区间", in = ParameterIn.QUERY, schema = @Schema(ref = "DateRangeSearchRequestSchema"))

    })
    @ApiResponse(responseCode = "200", description = "用户导出")
    @PreAuthorize("hasAuthority('user/export')")
    public void actionExport(
            @Parameter(hidden = true) UserSearchRequest userSearchRequest,
            HttpServletResponse response
    ) {
        try {
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel;charset=utf-8");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + URLEncoder.encode("用户列表.xlsx", StandardCharsets.UTF_8) + "\"");

            this.userUseCase.export(userSearchRequest, response.getOutputStream());

            //response.setHeader(HttpHeaders.CONTENT_LENGTH, );

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
        return this.userUseCase.read(id);
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

