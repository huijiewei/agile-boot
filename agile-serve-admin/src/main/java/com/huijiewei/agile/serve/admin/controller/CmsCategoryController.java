package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.cms.application.port.inbound.CmsCategoryUseCase;
import com.huijiewei.agile.app.cms.application.request.CmsCategoryRequest;
import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;
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
@Tag(name = "cms-category", description = "内容分类接口")
@RequiredArgsConstructor
public class CmsCategoryController {
    private final CmsCategoryUseCase cmsCategoryUseCase;

    @GetMapping(
            value = "/cms-categories/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "分类详情", operationId = "cmsCategoryView")
    @ApiResponse(responseCode = "200", description = "内容分类")
    @ApiResponse(responseCode = "404", description = "内容分类不存在", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('cms-category/view', 'cms-category/edit', 'cms-category/delete')")
    public CmsCategoryEntity actionView(@PathVariable("id") Integer id, @RequestParam(required = false) Boolean withParents) {
        return this.cmsCategoryUseCase.loadById(id, withParents);
    }

    @PostMapping(
            value = "/cms-categories",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "分类新建", operationId = "cmsCategoryCreate")
    @ApiResponse(responseCode = "201", description = "内容分类")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('cms-category/create/:id')")
    public CmsCategoryEntity actionCreate(@RequestBody CmsCategoryRequest request) {
        return this.cmsCategoryUseCase.create(request);
    }

    @PutMapping(
            value = "/cms-categories/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "分类编辑", operationId = "cmsCategoryEdit")
    @ApiResponse(responseCode = "200", description = "内容分类")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('cms-category/edit/:id')")
    public CmsCategoryEntity actionEdit(@PathVariable("id") Integer id, @RequestBody CmsCategoryRequest request) {
        return this.cmsCategoryUseCase.update(id, request);
    }

    @DeleteMapping(
            value = "/cms-categories/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "分类删除", operationId = "cmsCategoryDelete")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", description = "分类不存在", ref = "NotFoundProblem")
    @ApiResponse(responseCode = "409", description = "分类不允许删除", ref = "ConflictProblem")
    @PreAuthorize("hasAnyAuthority('cms-category/delete')")
    public MessageResponse actionDelete(@PathVariable("id") Integer id) {
        this.cmsCategoryUseCase.deleteById(id);

        return MessageResponse.of("分类删除成功");
    }
}
