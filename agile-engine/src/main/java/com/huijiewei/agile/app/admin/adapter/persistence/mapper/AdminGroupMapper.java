package com.huijiewei.agile.app.admin.adapter.persistence.mapper;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminGroup;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminGroupMapper {
    AdminGroup toAdminGroup(AdminGroupEntity adminGroupEntity);

    @Mapping(target = "permissions", ignore = true)
    AdminGroupEntity toAdminGroupEntity(AdminGroup adminGroup);
}
