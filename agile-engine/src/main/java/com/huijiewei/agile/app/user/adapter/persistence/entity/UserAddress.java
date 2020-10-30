package com.huijiewei.agile.app.user.adapter.persistence.entity;

import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.core.adapter.persistence.AbstractJpaEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * @author huijiewei
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class UserAddress extends AbstractJpaEntity {
    private String name;

    private String alias;

    private String phone;

    private String address;

    private Integer userId;

    private String districtCode;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "districtCode", referencedColumnName = "code", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private List<District> districtPath;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private User user;
}
