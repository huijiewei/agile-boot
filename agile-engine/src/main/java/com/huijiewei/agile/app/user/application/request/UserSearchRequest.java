package com.huijiewei.agile.app.user.application.request;

import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.core.application.request.*;
import com.huijiewei.agile.core.consts.DateTimeRange;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class UserSearchRequest extends AbstractSearchRequest {
    private String name;
    private String phone;
    private String email;
    private String[] createdFrom;
    private String[] createdAtRange;

    public UserSearchRequest() {
        this
                .addSearchField(new KeywordSearchField().field("phone").label("手机号码"))
                .addSearchField(new KeywordSearchField().field("email").label("电子邮箱"))
                .addSearchField(new KeywordSearchField().field("name").label("名称"))
                .addSearchField(new SelectSearchField()
                        .field("createdFrom")
                        .label("注册来源")
                        .multiple(true)
                        .options(UserCreatedFrom.values())
                )
                .addSearchField(new DateTimeRangeSearchField()
                        .field("createdRange")
                        .rangeType("daterange")
                        .labelStart("注册开始日期")
                        .labelEnd("注册结束日期")
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
