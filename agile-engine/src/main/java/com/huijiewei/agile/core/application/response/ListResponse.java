package com.huijiewei.agile.core.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author huijiewei
 */

@Getter
@Setter
public class ListResponse<T> {
    @Schema(description = "记录列表")
    private List<T> items;
}
