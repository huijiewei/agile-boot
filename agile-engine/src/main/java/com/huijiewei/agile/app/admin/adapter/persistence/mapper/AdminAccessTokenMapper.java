package com.huijiewei.agile.app.admin.adapter.persistence.mapper;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminAccessToken;
import com.huijiewei.agile.app.admin.domain.AdminAccessTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminAccessTokenMapper {
    AdminAccessToken toAdminAccessToken(AdminAccessTokenEntity adminAccessTokenEntity);

    AdminAccessTokenEntity toAdminAccessTokenEntity(AdminAccessToken adminAccessToken);
}
