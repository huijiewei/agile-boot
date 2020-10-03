package com.huijiewei.agile.core.adapter.persistence;

import com.huijiewei.agile.core.application.response.PageResponse;
import org.springframework.data.domain.Page;

/**
 * @author huijiewei
 */

public class PaginationCover {
    public static PageResponse.Pagination toPagination(Page<?> page) {
        PageResponse.Pagination pagination = new PageResponse.Pagination();

        pagination.setCurrentPage(page.getNumber() + 1);
        pagination.setTotalCount(page.getTotalElements());
        pagination.setPageCount(page.getTotalPages());
        pagination.setPerPage(page.getSize());

        return pagination;
    }
}
