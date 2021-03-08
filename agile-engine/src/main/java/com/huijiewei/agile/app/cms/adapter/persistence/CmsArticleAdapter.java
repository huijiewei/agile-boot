package com.huijiewei.agile.app.cms.adapter.persistence;

import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsArticle;
import com.huijiewei.agile.app.cms.adapter.persistence.mapper.CmsArticleMapper;
import com.huijiewei.agile.app.cms.adapter.persistence.repository.CmsArticleRepository;
import com.huijiewei.agile.app.cms.adapter.persistence.repository.CmsArticleTagRepository;
import com.huijiewei.agile.app.cms.application.port.outbound.CmsArticlePersistencePort;
import com.huijiewei.agile.app.cms.application.request.CmsArticleSearchRequest;
import com.huijiewei.agile.app.cms.domain.CmsArticleEntity;
import com.huijiewei.agile.core.adapter.persistence.JpaPaginationMapper;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.consts.DateTimeRange;
import com.huijiewei.agile.core.until.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CmsArticleAdapter implements CmsArticlePersistencePort {
    private final CmsArticleRepository cmsArticleRepository;
    private final CmsArticleTagRepository cmsArticleTagRepository;
    private final CmsArticleMapper cmsArticleMapper;

    @Override
    public boolean existsByCmsCategoryIdIn(Collection<Integer> cmsCategoryId) {
        return this.cmsArticleRepository.existsByCmsCategoryIdIn(cmsCategoryId);
    }

    private Specification<CmsArticle> buildSpecification(CmsArticleSearchRequest searchRequest) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new LinkedList<Predicate>();

            if (StringUtils.isNotBlank(searchRequest.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + searchRequest.getTitle() + "%"));
            }

            var dateTimeRange = searchRequest.getCreatedAtDateTimeRange();

            if (dateTimeRange != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), dateTimeRange.getBegin(), dateTimeRange.getEnd()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public SearchPageResponse<CmsArticleEntity> search(CmsArticleSearchRequest searchRequest, com.huijiewei.agile.core.application.request.PageRequest pageRequest, Boolean withSearchFields) {
        var articlePage = this.cmsArticleRepository.findAll(
                this.buildSpecification(searchRequest),
                PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(Sort.Direction.DESC, "id"))
        );

        var articleEntityResponses = new SearchPageResponse<CmsArticleEntity>();

        articleEntityResponses.setItems(articlePage
                .getContent()
                .stream()
                .map(this.cmsArticleMapper::toCmsArticleEntity)
                .collect(Collectors.toList()));

        articleEntityResponses.setPages(JpaPaginationMapper.toPagination(articlePage));

        if (withSearchFields != null && withSearchFields) {
            articleEntityResponses.setSearchFields(searchRequest.getSearchFields());
        }

        return articleEntityResponses;
    }

    @Override
    public Optional<CmsArticleEntity> getById(Integer id) {
        return this.cmsArticleRepository.findById(id).map(this.cmsArticleMapper::toCmsArticleEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(CmsArticleEntity cmsArticleEntity) {
        var cmsArticle = this.cmsArticleRepository.save(this.cmsArticleMapper.toCmsArticle(cmsArticleEntity));

        return cmsArticle.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.cmsArticleTagRepository.deleteByCmsArticleId(id);
        this.cmsArticleRepository.deleteById(id);
    }
}
