package com.huijiewei.agile.app.admin.domain;

import com.huijiewei.agile.core.domain.AbstractIdentityLogEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class AdminLogEntity extends AbstractIdentityLogEntity {
    private Integer adminId;

    @Schema(description = "所属管理员")
    private AdminEntity admin;
}
