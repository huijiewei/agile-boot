package com.huijiewei.agile.app.admin.domain;

import com.huijiewei.agile.core.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminAccessTokenEntity extends AbstractEntity {
    private Integer adminId;
    private String clientId;
    private String accessToken;
    private String remoteAddr;
    private String userAgent;
}
