package com.huijiewei.agile.core.application.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class BrSearchField extends BaseSearchField<BrSearchField> {
    public BrSearchField() {
        this.setType("br");
    }

    @Override
    protected BrSearchField self() {
        return this;
    }
}
