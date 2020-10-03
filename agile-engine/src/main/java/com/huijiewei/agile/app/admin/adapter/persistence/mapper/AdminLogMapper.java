package com.huijiewei.agile.app.admin.adapter.persistence.mapper;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminLog;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminLogMapper {
    AdminLogEntity toAdminLogEntity(AdminLog adminLog);

    AdminLog toAdminLog(AdminLogEntity adminLogEntity);
}

