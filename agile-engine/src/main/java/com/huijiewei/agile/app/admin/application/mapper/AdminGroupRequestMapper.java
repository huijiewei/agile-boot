package com.huijiewei.agile.app.admin.application.mapper;

import com.huijiewei.agile.app.admin.application.request.AdminGroupRequest;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface AdminGroupRequestMapper {
    @Mapping(target = "id", ignore = true)
    AdminGroupEntity toAdminGroupEntity(AdminGroupRequest adminGroupRequest);

    @InheritConfiguration
    void updateAdminGroupEntity(AdminGroupRequest adminGroupRequest, @MappingTarget AdminGroupEntity adminGroupEntity);
}
