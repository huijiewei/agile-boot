package com.huijiewei.agile.app.user.application.mapper;

import com.huijiewei.agile.app.user.application.request.UserRequest;
import com.huijiewei.agile.app.user.domain.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper {
    @Mapping(target = "password", ignore = true)
    UserEntity toUserEntity(UserRequest userRequest);

    @Mapping(target = "password", ignore = true)
    UserEntity toUserEntity(UserRequest userRequest, @MappingTarget UserEntity userEntity);
}
