package com.huijiewei.agile.app.open.domain;

import com.huijiewei.agile.core.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class CaptchaEntity extends AbstractEntity {
    private String uuid;

    private String code;

    private String userAgent;

    private String remoteAddr;

    private LocalDateTime createdAt;
}
