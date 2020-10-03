package com.huijiewei.agile.core.application.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class DateTimeRangeSearchField extends BaseSearchField<DateTimeRangeSearchField> {
    private String labelStart;
    private String labelEnd;
    private String rangeType;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DateTimeRangeShortcut> shortcuts;

    public DateTimeRangeSearchField() {
        this.setType("dateTimeRange");
    }

    public DateTimeRangeSearchField rangeType(String rangeType) {
        this.rangeType = rangeType;

        return this;
    }

    public DateTimeRangeSearchField labelStart(String labelStart) {
        this.labelStart = labelStart;

        return this;
    }

    public DateTimeRangeSearchField labelEnd(String labelEnd) {
        this.labelEnd = labelEnd;

        return this;
    }

    public DateTimeRangeSearchField addShortcut(String text, String start, String end) {
        if (this.shortcuts == null) {
            this.shortcuts = new ArrayList<>();
        }

        this.shortcuts.add(new DateTimeRangeShortcut(text, start, end));

        return this;
    }

    @Override
    protected DateTimeRangeSearchField self() {
        return this;
    }

    @Data
    public static class DateTimeRangeShortcut {
        private String text;
        private String start;
        private String end;

        public DateTimeRangeShortcut(String text, String start, String end) {
            this.text = text;
            this.start = start;
            this.end = end;
        }
    }
}
