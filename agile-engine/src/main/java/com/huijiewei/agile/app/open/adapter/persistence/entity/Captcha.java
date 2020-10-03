package com.huijiewei.agile.app.open.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
public class Captcha extends AbstractJpaEntity {
    private String uuid;

    private String code;

    private String userAgent;

    private String remoteAddr;

    private LocalDateTime createdAt;
}
