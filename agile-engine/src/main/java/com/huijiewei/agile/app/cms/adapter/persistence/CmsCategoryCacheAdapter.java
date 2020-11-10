package com.huijiewei.agile.app.cms.adapter.persistence;

import com.huijiewei.agile.app.cms.adapter.persistence.mapper.CmsCategoryMapper;
import com.huijiewei.agile.app.cms.adapter.persistence.repository.CmsCategoryRepository;
import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@RequiredArgsConstructor
public class CmsCategoryCacheAdapter {
    public static final String CATEGORIES_CACHE_KEY = "cms-categories";
    public static final String CATEGORY_TREE_CACHE_KEY = "cms-category-tree";

    private final CmsCategoryRepository cmsCategoryRepository;
    private final CmsCategoryMapper cmsCategoryMapper;

    @Cacheable(cacheNames = CATEGORIES_CACHE_KEY)
    public List<CmsCategoryEntity> getAll() {
        return this.cmsCategoryRepository
                .findAll()
                .stream()
                .map(this.cmsCategoryMapper::toCmsCategoryEntity)
                .collect(Collectors.toList());
    }
}
