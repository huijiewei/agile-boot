package com.huijiewei.agile.app.user.application.mapper;

import com.huijiewei.agile.app.user.application.request.UserRequest;
import com.huijiewei.agile.app.user.domain.UserEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author huijiewei
 */

@Mapper
public interface UserRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdIp", ignore = true)
    @Mapping(target = "createdFrom", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity toUserEntity(UserRequest userRequest);

    @InheritConfiguration
    void updateUserEntity(UserRequest userRequest, @MappingTarget UserEntity userEntity);
}
