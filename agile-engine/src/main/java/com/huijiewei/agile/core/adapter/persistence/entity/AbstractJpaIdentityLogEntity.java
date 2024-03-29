package com.huijiewei.agile.core.adapter.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

/**
 * @author huijiewei
 */
@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class AbstractJpaIdentityLogEntity extends AbstractJpaEntity {
    private String type;
    private Integer status;
    private String method;
    private String action;
    private String params;
    private String userAgent;
    private String remoteAddr;
    private String exception;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
