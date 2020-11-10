package com.huijiewei.agile.app.cms.adapter.persistence.mapper;

import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsCategory;
import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author huijiewei
 */

@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface CmsCategoryMapper {
    @Mapping(target = "parents", ignore = true)
    @Mapping(target = "children", ignore = true)
    CmsCategoryEntity toCmsCategoryEntity(CmsCategory cmsCategory);

    CmsCategory toCmsCategory(CmsCategoryEntity cmsCategoryEntity);
}
