package com.huijiewei.agile.app.user.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaIdentityEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

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

    @Column(updatable = false, insertable = false)
    private LocalDateTime updatedAt;
}
