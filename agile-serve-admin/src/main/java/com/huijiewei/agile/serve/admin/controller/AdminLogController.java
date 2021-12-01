package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.admin.application.port.inbound.AdminLogUseCase;
import com.huijiewei.agile.app.admin.application.request.AdminLogSearchRequest;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huijiewei
 */

@RestController
@Tag(name = "admin-log", description = "日志")
@RequiredArgsConstructor
public class AdminLogController {
    private final AdminLogUseCase adminLogUseCase;

    @GetMapping(
            value = "/admin-logs",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "操作日志", operationId = "userIndex")
    @ApiResponse(responseCode = "200", description = "操作日志")
    @PreAuthorize("hasAnyAuthority('admin-log/index')")
    public SearchPageResponse<AdminLogEntity> actionIndex(
            @ParameterObject AdminLogSearchRequest request,
            @Parameter(schema = @Schema(ref = "PageableRequestSchema")) @RequestParam(required = false) Pageable pageable,
            @Parameter(description = "是否返回搜索字段信息") @RequestParam(required = false) Boolean withSearchFields
    ) {
        return this.adminLogUseCase.search(request, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), withSearchFields);
    }
}
