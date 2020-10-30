package com.huijiewei.agile.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractIdentityEntity extends AbstractEntity {
    @Schema(description = "手机号码")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

    @Schema(description = "电子邮箱")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonIgnore
    private String password;
}
