package com.huijiewei.agile.app.cms.application.request;

import com.huijiewei.agile.core.application.request.AbstractSearchRequest;
import com.huijiewei.agile.core.application.request.BrSearchField;
import com.huijiewei.agile.core.application.request.DateTimeRangeSearchField;
import com.huijiewei.agile.core.application.request.KeywordSearchField;
import com.huijiewei.agile.core.consts.DateTimeRange;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class CmsArticleSearchRequest extends AbstractSearchRequest {
    private String title;
    private String[] createdAtRange;

    public CmsArticleSearchRequest() {
        this
                .addSearchField(new KeywordSearchField().field("title").label("标题"))
                .addSearchField(new DateTimeRangeSearchField()
                        .field("createdRange")
                        .rangeType("daterange")
                        .labelStart("创建开始日期")
                        .labelEnd("创建结束日期")
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

    public DateTimeRange getCreatedAtDateTimeRange() {
        return DateTimeRange.parse(createdAtRange);
    }
}
