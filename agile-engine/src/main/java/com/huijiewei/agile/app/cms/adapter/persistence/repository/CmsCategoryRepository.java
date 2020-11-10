package com.huijiewei.agile.app.cms.adapter.persistence.repository;

import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

@Repository
public interface CmsCategoryRepository extends
        JpaRepository<CmsCategory, Integer>,
        JpaSpecificationExecutor<CmsCategory> {
    @Modifying
    @Query("DELETE FROM CmsCategory WHERE id IN ?1")
    void deleteAllById(List<Integer> ids);

    Optional<CmsCategory> findBySlug(String slug);
}
