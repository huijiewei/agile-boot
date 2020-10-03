package com.huijiewei.agile.app.user.adapter.persistence.mapper;

import com.huijiewei.agile.app.user.adapter.persistence.entity.User;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.app.user.domain.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserEntity toUserEntity(User user);

    @Mapping(target = "password", ignore = true)
    User toUser(UserEntity userEntity);

    default UserCreatedFrom toCreatedFrom(String createdFrom) {
        return UserCreatedFrom.valueOf(createdFrom);
    }

    default String toCreatedFrom(UserCreatedFrom createdFrom) {
        return createdFrom.toString();
    }
}
