package com.huijiewei.agile.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huijiewei.agile.core.constraint.NotFalse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NotFalse(messages = "不能选择自己作为上级", properties = "parentId", verifiers = "verifyParentId")
public class AbstractTreeEntity<T extends AbstractTreeEntity<T>> extends AbstractEntity {
    private Integer parentId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<T> parents;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<T> children;

    public void addChild(T child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }

        this.children.add(child);
    }

    public boolean getVerifyParentId() {
        return !this.getParentId().equals(this.getId());
    }
}
