package com.huijiewei.agile.app.user.application.mapper;

import com.huijiewei.agile.app.user.application.request.UserRequest;
import com.huijiewei.agile.app.user.domain.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface UserRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdIp", ignore = true)
    @Mapping(target = "createdFrom", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity toUserEntity(UserRequest userRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdIp", ignore = true)
    @Mapping(target = "createdFrom", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateUserEntity(UserRequest userRequest, @MappingTarget UserEntity userEntity);
}
