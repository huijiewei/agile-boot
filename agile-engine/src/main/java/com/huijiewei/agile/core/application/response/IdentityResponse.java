package com.huijiewei.agile.core.application.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huijiewei.agile.core.domain.AbstractIdentityEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author huijiewei
 */

@Getter
@Setter
public class IdentityResponse<T extends AbstractIdentityEntity> {
    @Schema(description = "当前用户")
    private T currentUser;

    @Schema(description = "访问令牌")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String accessToken;

}
