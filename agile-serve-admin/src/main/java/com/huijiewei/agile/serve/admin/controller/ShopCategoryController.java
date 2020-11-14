package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.shop.application.port.inbound.ShopCategoryUseCase;
import com.huijiewei.agile.app.shop.application.request.ShopCategoryRequest;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
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
@Tag(name = "shop-category", description = "商品分类接口")
@RequiredArgsConstructor
public class ShopCategoryController {
    private final ShopCategoryUseCase shopCategoryUseCase;

    @GetMapping(
            value = "/shop-categories/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "分类详情", operationId = "shopCategoryView")
    @ApiResponse(responseCode = "200", description = "商品分类")
    @ApiResponse(responseCode = "404", description = "商品分类不存在", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('shop-category/view', 'shop-category/edit', 'shop-category/delete')")
    public ShopCategoryEntity actionView(@PathVariable("id") Integer id, @RequestParam(required = false) Boolean withParents) {
        return this.shopCategoryUseCase.loadById(id, withParents);
    }

    @PostMapping(
            value = "/shop-categories",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "分类新建", operationId = "shopCategoryCreate")
    @ApiResponse(responseCode = "201", description = "商品分类")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('shop-category/create/:id')")
    public ShopCategoryEntity actionCreate(@RequestBody ShopCategoryRequest request) {
        return this.shopCategoryUseCase.create(request);
    }

    @PutMapping(
            value = "/shop-categories/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "分类编辑", operationId = "shopCategoryEdit")
    @ApiResponse(responseCode = "200", description = "分类")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('shop-category/edit/:id')")
    public ShopCategoryEntity actionEdit(@PathVariable("id") Integer id, @RequestBody ShopCategoryRequest request) {
        return this.shopCategoryUseCase.update(id, request);
    }

    @DeleteMapping(
            value = "/shop-categories/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "分类删除", operationId = "shopCategoryDelete")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", description = "分类不存在", ref = "NotFoundProblem")
    @ApiResponse(responseCode = "409", description = "分类不允许删除", ref = "ConflictProblem")
    @PreAuthorize("hasAnyAuthority('shop-category/delete')")
    public MessageResponse actionDelete(@PathVariable("id") Integer id) {
        this.shopCategoryUseCase.deleteById(id);

        return MessageResponse.of("分类删除成功");
    }
}
