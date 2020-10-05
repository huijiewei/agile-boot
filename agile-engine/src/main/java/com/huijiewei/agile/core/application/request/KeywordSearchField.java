package com.huijiewei.agile.core.application.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class KeywordSearchField extends AbstractSearchField<KeywordSearchField> {
    public KeywordSearchField() {
        this.setType("keyword");
    }

    @Override
    protected KeywordSearchField self() {
        return this;
    }
}
