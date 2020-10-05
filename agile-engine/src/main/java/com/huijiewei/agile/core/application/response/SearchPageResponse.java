package com.huijiewei.agile.core.application.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huijiewei.agile.core.application.request.BaseSearchField;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author huijiewei
 */

public class SearchPageResponse<T> extends PageResponse<T> {
    @Schema(description = "搜索字段信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BaseSearchField<?>> searchFields;

    public List<BaseSearchField<?>> getSearchFields() {
        return this.searchFields;
    }

    public void setSearchFields(List<BaseSearchField<?>> searchFields) {
        this.searchFields = searchFields;
    }
}