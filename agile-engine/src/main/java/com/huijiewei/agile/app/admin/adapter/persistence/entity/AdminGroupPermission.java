package com.huijiewei.agile.app.admin.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class AdminGroupPermission extends AbstractJpaEntity {
    private Integer adminGroupId;

    private String actionId;
}
