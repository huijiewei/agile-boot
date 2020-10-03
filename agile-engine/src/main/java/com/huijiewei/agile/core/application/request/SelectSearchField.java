package com.huijiewei.agile.core.application.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class SelectSearchField extends BaseSearchField<SelectSearchField> {
    private Boolean multiple = false;
    private List<?> options;

    public SelectSearchField() {
        this.setType("select");
    }

    public SelectSearchField multiple(Boolean multiple) {
        this.multiple = multiple;

        return this;
    }

    public <E> SelectSearchField options(E[] options) {
        this.options = Arrays.asList(options);

        return this;
    }

    public <T> SelectSearchField options(List<T> options) {
        this.options = options;

        return this;
    }

    @Override
    protected SelectSearchField self() {
        return this;
    }
}
