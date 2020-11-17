package com.huijiewei.agile.core.application.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huijiewei.agile.core.application.request.AbstractSearchField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author huijiewei
 */

@Getter
@Setter
public class SearchPageResponse<T> extends PageResponse<T> {
    @Schema(description = "搜索字段信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AbstractSearchField<?>> searchFields;
}
