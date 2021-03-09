package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.user.application.port.inbound.UserAddressUseCase;
import com.huijiewei.agile.app.user.application.request.UserAddressRequest;
import com.huijiewei.agile.app.user.application.request.UserAddressSearchRequest;
import com.huijiewei.agile.app.user.domain.UserAddressEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.MessageResponse;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author huijiewei
 */

@RestController
@Tag(name = "user-address", description = "用户地址接口")
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressUseCase userAddressUseCase;

    @GetMapping(
            value = "/user-addresses",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户地址列表", operationId = "userAddressIndex", parameters = {
            @Parameter(name = "name", description = "联系人", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "phone", description = "联系方式", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "userName", description = "用户名称", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "userPhone", description = "用户手机号码", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "userEmail", description = "用户电子邮箱", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    })
    @ApiResponse(responseCode = "200", description = "用户地址列表")
    @PreAuthorize("hasAuthority('user-address/index')")
    public SearchPageResponse<UserAddressEntity> actionIndex(
            @Parameter(description = "是否返回搜索字段信息") @RequestParam(required = false) Boolean withSearchFields,
            @Parameter(hidden = true) UserAddressSearchRequest request,
            Pageable pageable
    ) {
        return this.userAddressUseCase.search(request, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), withSearchFields);
    }

    @GetMapping(
            value = "/user-addresses/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户地址详情", operationId = "userAddressView")
    @ApiResponse(responseCode = "200", description = "用户")
    @ApiResponse(responseCode = "404", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('user-address/view/:id', 'user/edit/:id')")
    public UserAddressEntity actionView(@PathVariable("id") Integer id) {
        return this.userAddressUseCase.loadById(id);
    }

    @PostMapping(
            value = "/user-addresses",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户地址新建", operationId = "userAddressCreate")
    @ApiResponse(responseCode = "201", description = "用户地址")
    @ApiResponse(responseCode = "422", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('user/create')")
    public UserAddressEntity actionCreate(@RequestBody UserAddressRequest request) {
        return this.userAddressUseCase.create(request);
    }

    @PutMapping(
            value = "/user-addresses/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户地址编辑", operationId = "userAddressEdit")
    @ApiResponse(responseCode = "200", description = "用户")
    @ApiResponse(responseCode = "404", ref = "NotFoundProblem")
    @ApiResponse(responseCode = "422", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('user-address/edit/:id')")
    public UserAddressEntity actionEdit(@PathVariable("id") Integer id, @RequestBody UserAddressRequest request) {
        return this.userAddressUseCase.update(id, request);
    }

    @DeleteMapping(
            value = "/user-addresses/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "用户地址删除", operationId = "userAddressDelete")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('user-address/delete')")
    public MessageResponse actionDelete(@PathVariable("id") Integer id) {
        this.userAddressUseCase.deleteById(id);

        return MessageResponse.of("用户地址删除成功");
    }
}

