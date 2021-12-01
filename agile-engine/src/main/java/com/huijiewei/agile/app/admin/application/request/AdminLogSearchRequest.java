package com.huijiewei.agile.app.admin.application.request;

import com.huijiewei.agile.core.application.request.*;
import com.huijiewei.agile.core.consts.DateTimeRange;
import com.huijiewei.agile.core.consts.IdentityLogStatus;
import com.huijiewei.agile.core.consts.IdentityLogType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminLogSearchRequest extends AbstractSearchRequest {
    @Parameter(description = "管理员")
    private String admin;

    @Parameter(description = "日志类型", schema = @Schema(ref = "IdentityLogTypeRequestSchema"))
    private String[] type;

    @Parameter(description = "日志状态")
    private Integer status;

    @Parameter(description = "创建日期区间", schema = @Schema(ref = "DateRangeSearchRequestSchema"))
    private String[] createdAtRange;

    public AdminLogSearchRequest() {
        this.addSearchField(new KeywordSearchField().field("admin").label("管理员")).addSearchField(new SelectSearchField().field("type").label("日志类型").multiple(true).options(IdentityLogType.values())).addSearchField(new SelectSearchField().field("status").label("操作状态").options(IdentityLogStatus.values())).addSearchField(new DateTimeRangeSearchField().field("createdAtRange").rangeType("daterange").labelStart("开始日期").labelEnd("结束日期").addShortcut("最近一周", LocalDate.now().minusWeeks(1).toString(), LocalDate.now().toString()).addShortcut("最近一个月", LocalDate.now().minusMonths(1).toString(), LocalDate.now().toString()).addShortcut("最近三个月", LocalDate.now().minusMonths(3).toString(), LocalDate.now().toString()).addShortcut("最近一年", LocalDate.now().minusYears(1).toString(), LocalDate.now().toString())).addSearchField(new BrSearchField());
    }

    @Parameter(hidden = true)
    public DateTimeRange getCreatedAtDateTimeRange() {
        return DateTimeRange.parse(createdAtRange);
    }
}
