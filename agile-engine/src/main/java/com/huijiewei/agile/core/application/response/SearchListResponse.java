package com.huijiewei.agile.core.application.response;

import com.huijiewei.agile.core.application.request.AbstractSearchField;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author huijiewei
 */

public class SearchListResponse<T> extends ListResponse<T> {
    @Schema(description = "搜索字段信息")
    private List<AbstractSearchField<?>> searchFields;

    public List<AbstractSearchField<?>> getSearchFields() {
        return this.searchFields;
    }

    public void setSearchFields(List<AbstractSearchField<?>> searchFields) {
        this.searchFields = searchFields;
    }
}
