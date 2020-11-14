package com.huijiewei.agile.app.cms.application.mapper;

import com.huijiewei.agile.app.cms.application.request.CmsArticleRequest;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface CmsArticleRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cmsCategory", ignore = true)
    @Mapping(target = "cmsTags", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "admin", ignore = true)
    CmsArticleEntity toCmsArticleEntity(CmsArticleRequest cmsArticleRequest);

    @InheritConfiguration
    @Mapping(target = "adminId", ignore = true)
    void updateCmsArticleEntity(CmsArticleRequest cmsArticleRequest, @MappingTarget CmsArticleEntity cmsArticleEntity);

}
