package com.huijiewei.agile.app.shop.adapter.persistence;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopBrand;
import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopBrandCategory;
import com.huijiewei.agile.app.shop.adapter.persistence.mapper.ShopBrandMapper;
import com.huijiewei.agile.app.shop.adapter.persistence.repository.ShopBrandCategoryRepository;
import com.huijiewei.agile.app.shop.adapter.persistence.repository.ShopBrandRepository;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopBrandPersistencePort;
import com.huijiewei.agile.app.shop.application.port.outbound.ShopBrandUniquePort;
import com.huijiewei.agile.app.shop.application.request.ShopBrandSearchRequest;
import com.huijiewei.agile.app.shop.domain.ShopBrandEntity;
import com.huijiewei.agile.core.adapter.persistence.JpaSpecificationBuilder;
import com.huijiewei.agile.core.adapter.persistence.PaginationCover;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.until.CollectionUtils;
import com.huijiewei.agile.core.until.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopBrandAdapter implements ShopBrandUniquePort, ShopBrandPersistencePort {
    private final ShopBrandRepository shopBrandRepository;
    private final ShopBrandMapper shopBrandMapper;
    private final ShopBrandCategoryRepository shopBrandCategoryRepository;

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.shopBrandRepository.count(JpaSpecificationBuilder.buildUnique(values, primaryKey, primaryValue)) == 0;
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

        this.updateShopBrandCategories(shopBrand.getId(), shopBrandEntity.getShopCategoryIds(), shopBrandEntity.hasId());

        return shopBrand.getId();
    }

    private void updateShopBrandCategories(Integer id, List<Integer> shopCategoryIds, Boolean delete) {
        if (delete) {
            this.shopBrandCategoryRepository.deleteAllByShopBrandId(id);
        }

        if (CollectionUtils.isEmpty(shopCategoryIds)) {
            return;
        }

        List<ShopBrandCategory> shopBrandCategories = new ArrayList<>();

        for (Integer shopCategoryId : shopCategoryIds) {
            ShopBrandCategory shopBrandCategory = new ShopBrandCategory();
            shopBrandCategory.setShopBrandId(id);
            shopBrandCategory.setShopCategoryId(shopCategoryId);
            shopBrandCategories.add(shopBrandCategory);
        }

        this.shopBrandCategoryRepository.batchInsert(shopBrandCategories);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.shopBrandCategoryRepository.deleteAllByShopBrandId(id);
        this.shopBrandRepository.deleteById(id);
    }
}
