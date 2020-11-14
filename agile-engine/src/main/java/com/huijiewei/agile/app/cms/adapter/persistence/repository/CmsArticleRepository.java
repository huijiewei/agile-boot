package com.huijiewei.agile.app.cms.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsArticle;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author huijiewei
 */

@Repository
public interface CmsArticleRepository extends
        EntityGraphJpaRepository<CmsArticle, Integer>,
        EntityGraphJpaSpecificationExecutor<CmsArticle> {
    boolean existsByCmsCategoryIdIn(Collection<Integer> cmsCategoryIds);
}
