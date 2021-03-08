package com.huijiewei.agile.app.admin.adapter.persistence.mapper;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminLog;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.consts.IdentityLogStatus;
import com.huijiewei.agile.core.consts.IdentityLogType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface AdminLogMapper {
    @Mapping(target = "admin.adminGroup", ignore = true)
    @Mapping(target = "admin.adminGroupId", ignore = true)
    @Mapping(target = "admin.createdAt", ignore = true)
    @Mapping(target = "admin.email", ignore = true)
    @Mapping(target = "admin.phone", ignore = true)
    @Mapping(target = "admin.avatar", ignore = true)
    AdminLogEntity toAdminLogEntity(AdminLog adminLog);

    @Mapping(target = "admin", ignore = true)
    AdminLog toAdminLog(AdminLogEntity adminLogEntity);

    default IdentityLogType toType(String type) {
        return IdentityLogType.valueOf(type);
    }

    default String toType(IdentityLogType type) {
        return type.getValue();
    }

    default IdentityLogStatus toStatus(Integer status) {
        return IdentityLogStatus.valueOf(status);
    }

    default Integer toStatus(IdentityLogStatus status) {
        return status.getValue();
    }
}

