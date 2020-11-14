package com.huijiewei.agile.app.admin.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class AdminAccessToken extends AbstractJpaEntity {
    private Integer adminId;

    private String clientId;

    private String accessToken;

    private String remoteAddr;

    private String userAgent;

    @Column(insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}