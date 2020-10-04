package com.huijiewei.agile.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author huijiewei
 */

@Getter
@Setter
public abstract class AbstractEntity {
    @Schema(description = "Id")
    private Integer id;

    public Boolean hasId() {
        return this.getId() != null && this.getId() > 0;
    }
}
