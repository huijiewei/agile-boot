package com.huijiewei.agile.app.user.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaIdentityEntity;
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
public class User extends AbstractJpaIdentityEntity {
    private String name;

    private String avatar;

    private String createdIp;

    private String createdFrom;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
