package com.huijiewei.agile.app.admin.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.AbstractJpaIdentityEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Admin extends AbstractJpaIdentityEntity {
    private String name;

    private String avatar;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    private Integer adminGroupId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adminGroupId", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private AdminGroup adminGroup;
}
