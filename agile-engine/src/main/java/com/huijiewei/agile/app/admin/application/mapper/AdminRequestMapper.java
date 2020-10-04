package com.huijiewei.agile.app.admin.application.mapper;

import com.huijiewei.agile.app.admin.application.request.AdminRequest;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminRequestMapper {
    @Mapping(target = "password", ignore = true)
    AdminEntity toAdminEntity(AdminRequest adminRequest);

    @Mapping(target = "password", ignore = true)
    AdminEntity toAdminEntity(AdminRequest adminRequest, @MappingTarget AdminEntity adminEntity);
}
