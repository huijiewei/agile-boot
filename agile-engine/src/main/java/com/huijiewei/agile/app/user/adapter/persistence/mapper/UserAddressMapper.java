package com.huijiewei.agile.app.user.adapter.persistence.mapper;

import com.huijiewei.agile.app.user.adapter.persistence.entity.UserAddress;
import com.huijiewei.agile.app.user.domain.UserAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author huijiewei
 */

@Mapper
public interface UserAddressMapper {
    @Mapping(target = "user.createdFrom", ignore = true)
    @Mapping(target = "user.createdAt", ignore = true)
    @Mapping(target = "user.createdIp", ignore = true)
    @Mapping(target = "districtPath", ignore = true)
    UserAddressEntity toUserAddressEntity(UserAddress userAddress);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserAddress toUserAddress(UserAddressEntity userAddressEntity);
}
