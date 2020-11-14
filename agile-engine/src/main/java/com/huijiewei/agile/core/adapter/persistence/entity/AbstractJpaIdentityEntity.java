package com.huijiewei.agile.core.adapter.persistence.entity;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class AbstractJpaIdentityEntity extends AbstractJpaEntity {
    private String phone;

    private String email;

    private String password;
}
