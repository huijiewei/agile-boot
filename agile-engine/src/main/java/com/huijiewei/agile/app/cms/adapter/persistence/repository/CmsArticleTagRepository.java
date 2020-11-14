package com.huijiewei.agile.app.cms.adapter.persistence.repository;

import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsArticleTag;
import com.huijiewei.agile.core.adapter.persistence.BatchJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huijiewei
 */

@Repository
public interface CmsArticleTagRepository extends
        BatchJpaRepository<CmsArticleTag>,
        JpaRepository<CmsArticleTag, Integer> {
    void deleteByCmsArticleId(Integer cmsArticleId);
}
