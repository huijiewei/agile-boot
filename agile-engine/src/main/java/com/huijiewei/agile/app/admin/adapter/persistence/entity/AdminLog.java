package com.huijiewei.agile.app.admin.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaIdentityLogEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class AdminLog extends AbstractJpaIdentityLogEntity {
    private Integer adminId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adminId", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Admin admin;
}
