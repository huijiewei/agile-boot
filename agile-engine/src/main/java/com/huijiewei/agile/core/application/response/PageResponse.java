package com.huijiewei.agile.core.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author huijiewei
 */

@Getter
@Setter
public class PageResponse<T> extends ListResponse<T> {
    @Schema(description = "分页信息")
    private Pagination pages;

    @Getter
    @Setter
    public static class Pagination {
        private Long totalCount;
        private Integer currentPage;
        private Integer pageCount;
        private Integer perPage;
    }
}
