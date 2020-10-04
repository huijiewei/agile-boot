package com.huijiewei.agile.app.shop.adapter.persistence;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopBrand;
import com.huijiewei.agile.app.shop.adapter.persistence.mapper.ShopBrandMapper;
import com.huijiewei.agile.app.shop.adapter.persistence.repository.JpaShopBrandCategoryRepository;
import com.huijiewei.agile.app.shop.adapter.persistence.repository.JpaShopBrandRepository;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopBrandPersistencePort;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopBrandUniquePort;
import com.huijiewei.agile.app.shop.application.request.ShopBrandSearchRequest;
import com.huijiewei.agile.app.shop.domain.ShopBrandEntity;
import com.huijiewei.agile.core.adapter.persistence.PaginationCover;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
public class JpaShopBrandAdapter implements ShopBrandUniquePort, ShopBrandPersistencePort {
    private final JpaShopBrandRepository shopBrandRepository;
    private final ShopBrandMapper shopBrandMapper;
    private final JpaShopBrandCategoryRepository shopBrandCategoryRepository;

    public JpaShopBrandAdapter(JpaShopBrandRepository shopBrandRepository, ShopBrandMapper shopBrandMapper, JpaShopBrandCategoryRepository shopBrandCategoryRepository) {
        this.shopBrandRepository = shopBrandRepository;
        this.shopBrandMapper = shopBrandMapper;
        this.shopBrandCategoryRepository = shopBrandCategoryRepository;
    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        Specification<ShopBrand> shopBrandSpecification = (Specification<ShopBrand>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            for (Map.Entry<String, String> entry : values.entrySet()) {
                predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
            }

            if (StringUtils.isNotEmpty(primaryValue)) {
                predicates.add(criteriaBuilder.notEqual(root.get(primaryKey), primaryValue));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return this.shopBrandRepository.count(shopBrandSpecification) == 0;
    }

    private Specification<ShopBrand> buildSpecification(ShopBrandSearchRequest searchRequest) {
        return (Specification<ShopBrand>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (StringUtils.isNotBlank(searchRequest.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchRequest.getName() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public SearchPageResponse<ShopBrandEntity> getAll(Integer page, Integer size, ShopBrandSearchRequest searchRequest, Boolean withSearchFields) {
        Page<ShopBrand> shopBrandPage = this.shopBrandRepository.findAll(
                this.buildSpecification(searchRequest),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        );

        SearchPageResponse<ShopBrandEntity> shopBrandEntityResponses = new SearchPageResponse<>();

        shopBrandEntityResponses.setItems(shopBrandPage
                .getContent()
                .stream()
                .map(this.shopBrandMapper::toShopBrandEntity)
                .collect(Collectors.toList()));

        shopBrandEntityResponses.setPages(PaginationCover.toPagination(shopBrandPage));

        if (withSearchFields != null && withSearchFields) {
            shopBrandEntityResponses.setSearchFields(searchRequest.getSearchFields());
        }

        return shopBrandEntityResponses;
    }

    @Override
    public Optional<ShopBrandEntity> getById(Integer id) {
        return this.shopBrandRepository.findById(id).map(this.shopBrandMapper::toShopBrandEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(ShopBrandEntity shopBrandEntity) {
        ShopBrand shopBrand = this.shopBrandRepository.save(this.shopBrandMapper.toShopBrand(shopBrandEntity));

        return shopBrand.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.shopBrandCategoryRepository.deleteAllByShopBrandId(id);
        this.shopBrandRepository.deleteById(id);
    }
}
