package com.huijiewei.agile.app.admin.adapter.persistence.mapper;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminAccessToken;
import com.huijiewei.agile.app.admin.domain.AdminAccessTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author huijiewei
 */

@Mapper
public interface AdminAccessTokenMapper {
    @Mapping(target = "updatedAt", ignore = true)
    AdminAccessToken toAdminAccessToken(AdminAccessTokenEntity adminAccessTokenEntity);

    AdminAccessTokenEntity toAdminAccessTokenEntity(AdminAccessToken adminAccessToken);
}
