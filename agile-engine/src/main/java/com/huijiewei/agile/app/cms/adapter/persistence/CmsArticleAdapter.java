package com.huijiewei.agile.app.cms.adapter.persistence;

import com.huijiewei.agile.app.cms.adapter.persistence.repository.CmsArticleRepository;
import com.huijiewei.agile.app.cms.application.port.outbound.CmsArticlePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CmsArticleAdapter implements CmsArticlePersistencePort {
    private final CmsArticleRepository cmsArticleRepository;

    @Override
    public Boolean existsByCmsCategoryIds(List<Integer> cmsCategoryIds) {
        return this.cmsArticleRepository.existsByCmsCategoryIds(cmsCategoryIds);
    }
}
