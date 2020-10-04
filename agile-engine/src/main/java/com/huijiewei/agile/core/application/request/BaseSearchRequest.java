package com.huijiewei.agile.core.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@Data
public abstract class BaseSearchRequest {
    @Schema(hidden = true)
    private List<BaseSearchField<?>> searchFields;

    public BaseSearchRequest addSearchField(BaseSearchField<?> searchField) {
        if (this.searchFields == null) {
            this.searchFields = new ArrayList<>();
        }

        this.searchFields.add(searchField);

        return this;
    }
}
