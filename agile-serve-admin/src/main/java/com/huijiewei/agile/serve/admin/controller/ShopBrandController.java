package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.shop.application.port.inbound.ShopBrandUseCase;
import com.huijiewei.agile.app.shop.application.request.ShopBrandRequest;
import com.huijiewei.agile.app.shop.application.request.ShopBrandSearchRequest;
import com.huijiewei.agile.app.shop.domain.ShopBrandEntity;
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
@Tag(name = "shop-brand", description = "商品品牌接口")
@RequiredArgsConstructor
public class ShopBrandController {
    private final ShopBrandUseCase shopBrandUseCase;

    @GetMapping(
            value = "/shop-brands",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "品牌列表", operationId = "shopBrandIndex", parameters = {
            @Parameter(name = "name", description = "品牌名称", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "page", description = "分页页码", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "size", description = "分页大小", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
    })
    @ApiResponse(responseCode = "200", description = "商品品牌列表")
    @PreAuthorize("hasAnyAuthority('shop-brand/index')")
    public SearchPageResponse<ShopBrandEntity> actionIndex(
            @Parameter(description = "是否返回搜索字段信息") @RequestParam(required = false) Boolean withSearchFields,
            @Parameter(hidden = true) ShopBrandSearchRequest request,
            @Parameter(hidden = true) Pageable pageable) {
        return this.shopBrandUseCase.search(request, pageable.getPageNumber(), pageable.getPageSize(), withSearchFields);
    }

    @GetMapping(
            value = "/shop-brands/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "品牌详情", operationId = "shopBrandView")
    @ApiResponse(responseCode = "200", description = "商品品牌")
    @ApiResponse(responseCode = "404", description = "商品品牌不存在", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('shop-brand/view/:id', 'shop-brand/edit/:id')")
    public ShopBrandEntity actionView(@PathVariable("id") Integer id) {
        return this.shopBrandUseCase.loadById(id);
    }

    @PostMapping(
            value = "/shop-brands",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "品牌新建", operationId = "shopBrandCreate")
    @ApiResponse(responseCode = "201", description = "商品品牌")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('shop-brand/create')")
    public ShopBrandEntity actionCreate(@RequestBody ShopBrandRequest request) {
        return this.shopBrandUseCase.create(request);
    }

    @PutMapping(
            value = "/shop-brands/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "品牌编辑", operationId = "shopBrandEdit")
    @ApiResponse(responseCode = "200", description = "品牌")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('shop-brand/edit/:id')")
    public ShopBrandEntity actionEdit(@PathVariable("id") Integer id, @RequestBody ShopBrandRequest request) {
        return this.shopBrandUseCase.update(id, request);
    }

    @DeleteMapping(
            value = "/shop-brands/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "分类删除", operationId = "shopBrandDelete")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", description = "品牌不存在", ref = "NotFoundProblem")
    @ApiResponse(responseCode = "409", description = "品牌不允许删除", ref = "ConflictProblem")
    @PreAuthorize("hasAnyAuthority('shop-brand/delete')")
    public MessageResponse actionDelete(@PathVariable("id") Integer id) {
        this.shopBrandUseCase.deleteById(id);

        return MessageResponse.of("品牌删除成功");
    }
}

