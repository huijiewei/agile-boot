package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.application.request.AdminLogSearchRequest;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class AdminLogController {
    private final AdminLogPersistencePort adminLogPersistencePort;

    public AdminLogController(AdminLogPersistencePort adminLogPersistencePort) {
        this.adminLogPersistencePort = adminLogPersistencePort;
    }

    @GetMapping(
            value = "/admin-logs",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "操作日志", operationId = "userIndex", parameters = {
            @Parameter(name = "page", description = "分页页码", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "size", description = "分页大小", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
            @Parameter(name = "admin", description = "管理员", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
            @Parameter(name = "createdRange", description = "创建日期区间", in = ParameterIn.QUERY, schema = @Schema(ref = "DateRangeSearchRequestSchema"))
    })
    @ApiResponse(responseCode = "200", description = "操作日志")
    @PreAuthorize("hasPermission('ADMIN', 'admin-log/index')")
    public SearchPageResponse<AdminLogEntity> actionIndex(
            @Parameter(description = "是否返回搜索字段信息") @RequestParam(required = false) Boolean withSearchFields,
            @Parameter(hidden = true) AdminLogSearchRequest request,
            @Parameter(hidden = true) Pageable pageable
    ) {
        return this.adminLogPersistencePort.getAll(pageable.getPageNumber(), pageable.getPageSize(), request, withSearchFields);
    }
}
