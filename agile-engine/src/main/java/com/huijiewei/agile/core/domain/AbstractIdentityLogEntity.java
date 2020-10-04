package com.huijiewei.agile.core.domain;

import com.huijiewei.agile.core.consts.IdentityLogStatus;
import com.huijiewei.agile.core.consts.IdentityLogType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractIdentityLogEntity extends AbstractEntity {
    @Schema(description = "日志类型")
    private IdentityLogType type;

    @Schema(description = "操作状态")
    private IdentityLogStatus status;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "操作动作")
    private String action;

    @Schema(description = "访问参数")
    private String params;

    @Schema(description = "浏览器")
    private String userAgent;

    @Schema(description = "IP 地址")
    private String remoteAddr;

    @Schema(description = "异常信息")
    private String exception;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
