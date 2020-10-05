package com.huijiewei.agile.app.admin.application.mapper;

import com.huijiewei.agile.app.admin.application.request.AdminGroupRequest;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminGroupRequestMapper {
    AdminGroupEntity toAdminGroupEntity(AdminGroupRequest adminGroupRequest);

    void updateAdminGroupEntity(AdminGroupRequest adminGroupRequest, @MappingTarget AdminGroupEntity adminGroupEntity);
}
