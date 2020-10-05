package com.huijiewei.agile.app.admin.application.mapper;

import com.huijiewei.agile.app.admin.application.request.AdminRequest;
import com.huijiewei.agile.app.admin.domain.AdminEntity;
import org.mapstruct.*;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminRequestMapper {
    @Mapping(target = "password", ignore = true)
    AdminEntity toAdminEntity(AdminRequest adminRequest);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "adminGroupId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdminEntity(AdminRequest adminRequest, @MappingTarget AdminEntity adminEntity);
}
