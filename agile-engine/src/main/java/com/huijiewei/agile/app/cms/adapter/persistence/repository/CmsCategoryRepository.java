package com.huijiewei.agile.app.cms.adapter.persistence.repository;

import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * @author huijiewei
 */

@Repository
public interface CmsCategoryRepository extends
        JpaRepository<CmsCategory, Integer>,
        JpaSpecificationExecutor<CmsCategory> {
    void deleteByIdIn(Collection<Integer> ids);

    Optional<CmsCategory> findBySlug(String slug);
}
