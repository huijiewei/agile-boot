package com.huijiewei.agile.core.adapter.persistence.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
