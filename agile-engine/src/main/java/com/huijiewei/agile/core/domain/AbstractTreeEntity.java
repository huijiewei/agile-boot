package com.huijiewei.agile.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
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
}
