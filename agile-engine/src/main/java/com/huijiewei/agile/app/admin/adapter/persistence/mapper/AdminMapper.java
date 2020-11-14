package com.huijiewei.agile.app.admin.adapter.persistence.mapper;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.Admin;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface AdminMapper {
    @Mapping(target = "adminGroup.permissions", ignore = true)
    AdminEntity toAdminEntityWithAdminGroup(Admin admin);

    @Named("toAdminEntityBasic")
    @Mapping(target = "adminGroup", ignore = true)
    @Mapping(target = "adminGroupId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    AdminEntity toAdminEntityBasic(Admin admin);

    @Mapping(target = "adminGroup", ignore = true)
    AdminEntity toAdminEntity(Admin admin);

    @Mapping(target = "adminGroup", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Admin toAdmin(AdminEntity adminEntity);
}

