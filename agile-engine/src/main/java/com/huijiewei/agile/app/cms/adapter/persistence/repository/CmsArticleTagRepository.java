package com.huijiewei.agile.app.cms.adapter.persistence.repository;

import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsArticleTag;
import com.huijiewei.agile.core.adapter.persistence.BatchJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author huijiewei
 */

@Repository
public interface CmsArticleTagRepository extends
        BatchJpaRepository<CmsArticleTag>,
        JpaRepository<CmsArticleTag, Integer> {
    @Modifying
    @Query("DELETE FROM CmsArticleTag WHERE cmsArticleId = ?1")
    void deleteAllByCmsArticleId(Integer id);
}
