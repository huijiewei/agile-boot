package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.cms.application.port.inbound.CmsArticleUseCase;
import com.huijiewei.agile.app.cms.application.request.CmsArticleRequest;
import com.huijiewei.agile.app.cms.application.request.CmsArticleSearchRequest;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;
import com.huijiewei.agile.core.application.response.MessageResponse;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.serve.admin.security.AdminUserDetails;
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
@Tag(name = "cms-article", description = "文章接口")
@RequiredArgsConstructor
public class CmsArticleController {
    private final CmsArticleUseCase cmsArticleUseCase;

    @GetMapping(
            value = "/cms-articles",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "文章列表", operationId = "cmsArticleIndex", parameters = {
            @Parameter(name = "title", description = "文章标题", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "createdRange", description = "创建日期区间", in = ParameterIn.QUERY, schema = @Schema(ref = "DateRangeSearchRequestSchema")),
            @Parameter(name = "page", description = "分页页码", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "size", description = "分页大小", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
    })
    @ApiResponse(responseCode = "200", description = "文章列表")
    @PreAuthorize("hasAnyAuthority('cms-article/index')")
    public SearchPageResponse<CmsArticleEntity> actionIndex(
            @Parameter(description = "是否返回搜索字段信息") @RequestParam(required = false) Boolean withSearchFields,
            @Parameter(hidden = true) CmsArticleSearchRequest request,
            @Parameter(hidden = true) Pageable pageable) {
        return this.cmsArticleUseCase.search(request, pageable.getPageNumber(), pageable.getPageSize(), withSearchFields);
    }

    @GetMapping(
            value = "/cms-articles/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "文章详情", operationId = "cmsArticleView")
    @ApiResponse(responseCode = "200", description = "文章")
    @ApiResponse(responseCode = "404", description = "文章不存在", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('cms-article/view/:id', 'cms-article/edit/:id')")
    public CmsArticleEntity actionView(@PathVariable("id") Integer id) {
        return this.cmsArticleUseCase.loadById(id);
    }

    @PostMapping(
            value = "/cms-articles",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "文章新建", operationId = "cmsArticleCreate")
    @ApiResponse(responseCode = "201", description = "文章")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('cms-article/create')")
    public CmsArticleEntity actionCreate(@RequestBody CmsArticleRequest request) {
        request.setAdminId(AdminUserDetails.getCurrentAdminIdentity().getAdminEntity().getId());

        return this.cmsArticleUseCase.create(request);
    }

    @PutMapping(
            value = "/cms-articles/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "文章编辑", operationId = "cmsArticleEdit")
    @ApiResponse(responseCode = "200", description = "文章")
    @ApiResponse(responseCode = "422", description = "输入验证错误", ref = "UnprocessableEntityProblem")
    @PreAuthorize("hasAnyAuthority('cms-article/edit/:id')")
    public CmsArticleEntity actionEdit(@PathVariable("id") Integer id, @RequestBody CmsArticleRequest request) {
        return this.cmsArticleUseCase.update(id, request);
    }

    @DeleteMapping(
            value = "/cms-articles/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "文章删除", operationId = "cmsArticleDelete")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @ApiResponse(responseCode = "404", description = "文章不存在", ref = "NotFoundProblem")
    @PreAuthorize("hasAnyAuthority('cms-article/delete')")
    public MessageResponse actionDelete(@PathVariable("id") Integer id) {
        this.cmsArticleUseCase.deleteById(id);

        return MessageResponse.of("文章删除成功");
    }
}

