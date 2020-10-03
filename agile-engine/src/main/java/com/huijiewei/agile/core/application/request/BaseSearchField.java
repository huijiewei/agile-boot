package com.huijiewei.agile.core.application.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * @author huijiewei
 */

@Data
@MappedSuperclass
public abstract class BaseSearchField<T extends BaseSearchField<T>> {
    private String type;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String field;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String label;

    public T field(String field) {
        this.field = field;

        return self();
    }

    public T label(String label) {
        this.label = label;

        return self();
    }

    /**
     * 返回自身类
     *
     * @return 返回自身类
     */
    abstract protected T self();
}
