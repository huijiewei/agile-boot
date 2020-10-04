package com.huijiewei.agile.app.admin.application.request;

import com.huijiewei.agile.core.application.request.*;
import com.huijiewei.agile.core.consts.IdentityLogStatus;
import com.huijiewei.agile.core.consts.IdentityLogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminLogSearchRequest extends BaseSearchRequest {
    private String admin;
    private String[] type;
    private Integer status;
    private String[] createdRange;

    public AdminLogSearchRequest() {
        this
                .addSearchField(new KeywordSearchField().field("admin").label("管理员"))
                .addSearchField(new SelectSearchField()
                        .field("type")
                        .label("日志类型")
                        .multiple(true)
                        .options(IdentityLogType.values())
                )
                .addSearchField(new SelectSearchField()
                        .field("status")
                        .label("操作状态")
                        .options(IdentityLogStatus.values())
                )
                .addSearchField(new DateTimeRangeSearchField()
                        .field("createdRange")
                        .rangeType("daterange")
                        .labelStart("开始日期")
                        .labelEnd("结束日期")
                        .addShortcut(
                                "最近一周",
                                LocalDate.now().minusWeeks(1).toString(),
                                LocalDate.now().toString()
                        )
                        .addShortcut(
                                "最近一个月",
                                LocalDate.now().minusMonths(1).toString(),
                                LocalDate.now().toString()
                        )
                        .addShortcut(
                                "最近三个月",
                                LocalDate.now().minusMonths(3).toString(),
                                LocalDate.now().toString()
                        )
                        .addShortcut(
                                "最近一年",
                                LocalDate.now().minusYears(1).toString(),
                                LocalDate.now().toString()
                        )
                )
                .addSearchField(new BrSearchField());
    }
}
