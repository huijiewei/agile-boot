package com.huijiewei.agile.core.application.request;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author huijiewei
 */

@Data
public class PageRequest {
    private final int page;
    private final int size;

    public static PageRequest of(Integer page, Integer size) {
        return new PageRequest(page, size);
    }
}
