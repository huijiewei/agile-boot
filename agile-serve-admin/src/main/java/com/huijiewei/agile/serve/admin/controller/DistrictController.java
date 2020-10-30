package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.district.application.port.inbound.DistrictUseCase;
import com.huijiewei.agile.app.district.application.request.DistrictRequest;
import com.huijiewei.agile.app.district.domain.DistrictEntity;
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
@Tag(name = "district", description = "地区接口")
@RequiredArgsConstructor
public class DistrictController {
    private final DistrictUseCase districtUseCase;

    @PostMapping(
            value = "/districts",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "地区新建", operationId = "districtCreate")
    @ApiResponse(responseCode = "201", description = "地区")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('district/create/:id')")
    public DistrictEntity actionCreate(@RequestBody DistrictRequest request) {
        return this.districtUseCase.create(request);
    }

    @GetMapping(
            value = "/districts/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "地区", operationId = "districtView")
    @ApiResponse(responseCode = "200", description = "地区")
    @ApiResponse(responseCode = "404", description = "地区不存在", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('district/view', 'district/edit', 'district/delete')")
    public DistrictEntity actionView(@PathVariable("id") Integer id, @RequestParam(required = false) Boolean withParents) {
        return this.districtUseCase.read(id, withParents);
    }

    @PutMapping(
            value = "/districts/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "地区编辑", operationId = "districtEdit")
    @ApiResponse(responseCode = "200", description = "地区")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('district/edit/:id')")
    public DistrictEntity actionEdit(@PathVariable("id") Integer id, @RequestBody DistrictRequest request) {
        return this.districtUseCase.update(id, request);
    }

    @DeleteMapping(
            value = "/districts/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "地区删除", operationId = "districtDelete")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", description = "地区不存在", ref = "NotFoundProblem")
    @ApiResponse(responseCode = "409", description = "地区不允许删除", ref = "ConflictProblem")
    @PreAuthorize("hasAnyAuthority('district/delete')")
    public MessageResponse actionDelete(@PathVariable("id") Integer id) {
        this.districtUseCase.deleteById(id);

        return MessageResponse.of("地区删除成功");
    }
}
