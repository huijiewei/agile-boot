package com.huijiewei.agile.core.application.request;

import lombok.Data;

/**
 * @author huijiewei
 */

@Data
public class PageRequest {
    private final int page;
    private final int size;

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }
}
