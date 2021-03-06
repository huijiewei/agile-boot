package com.huijiewei.agile.app.user.application.mapper;

import com.huijiewei.agile.app.user.application.request.UserAddressRequest;
import com.huijiewei.agile.app.user.domain.UserAddressEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author huijiewei
 */

@Mapper
public interface UserAddressRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "districtPath", ignore = true)
    @Mapping(target = "districtAddress", ignore = true)
    @Mapping(target = "fullAddress", ignore = true)
    UserAddressEntity toUserAddressEntity(UserAddressRequest userAddressRequest);

    @InheritConfiguration
    void updateUserAddressEntity(UserAddressRequest userAddressRequest, @MappingTarget UserAddressEntity userAddressEntity);
}
