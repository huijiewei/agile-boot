package com.huijiewei.agile.app.admin.application.mapper;

import com.huijiewei.agile.app.admin.application.request.AdminRequest;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import org.mapstruct.*;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface AdminRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "adminGroup", ignore = true)
    AdminEntity toAdminEntity(AdminRequest adminRequest);

    @InheritConfiguration
    @Mapping(target = "adminGroupId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdminEntity(AdminRequest adminRequest, @MappingTarget AdminEntity adminEntity);
}
