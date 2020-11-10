package com.huijiewei.agile.app.cms.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsArticle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huijiewei
 */

@Repository
public interface CmsArticleRepository extends
        EntityGraphJpaRepository<CmsArticle, Integer>,
        EntityGraphJpaSpecificationExecutor<CmsArticle> {
    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END FROM CmsArticle WHERE cmsCategoryId IN ?1")
    Boolean existsByCmsCategoryIds(List<Integer> cmsCategoryIds);
}
