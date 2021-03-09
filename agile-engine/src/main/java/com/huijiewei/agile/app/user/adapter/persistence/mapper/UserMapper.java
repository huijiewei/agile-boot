package com.huijiewei.agile.app.user.adapter.persistence.mapper;

import com.huijiewei.agile.app.user.adapter.persistence.entity.User;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.app.user.domain.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author huijiewei
 */

@Mapper
public interface UserMapper {
    UserEntity toUserEntity(User user);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserEntity userEntity);

    default UserCreatedFrom toCreatedFrom(String createdFrom) {
        return UserCreatedFrom.valueOf(createdFrom);
    }

    default String toCreatedFrom(UserCreatedFrom createdFrom) {
        return createdFrom.getValue();
    }
}
