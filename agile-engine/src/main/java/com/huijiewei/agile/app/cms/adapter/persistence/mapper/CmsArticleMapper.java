package com.huijiewei.agile.app.cms.adapter.persistence.mapper;

import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsArticle;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author huijiewei
 */

@Mapper
public interface CmsArticleMapper {
    @Mapping(target = "cmsCategoryId", ignore = true)
    @Mapping(target = "admin.adminGroup", ignore = true)
    @Mapping(target = "cmsCategory.parents", ignore = true)
    @Mapping(target = "cmsCategory.children", ignore = true)
    CmsArticleEntity toCmsArticleEntity(CmsArticle cmsArticle);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "cmsCategory", ignore = true)
    @Mapping(target = "admin", ignore = true)
    CmsArticle toCmsArticle(CmsArticleEntity cmsArticleEntity);
}
