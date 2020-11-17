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
public class SelectSearchField extends AbstractSearchField<SelectSearchField> {
    private boolean multiple = false;
    private List<?> options;

    public SelectSearchField() {
        this.setType("select");
    }

    public SelectSearchField multiple(boolean multiple) {
        this.multiple = multiple;

        return this;
    }

    public <E> SelectSearchField options(E[] options) {
        return this.options(Arrays.asList(options));
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
