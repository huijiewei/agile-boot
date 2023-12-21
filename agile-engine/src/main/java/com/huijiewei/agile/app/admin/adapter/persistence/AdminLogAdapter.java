package com.huijiewei.agile.app.admin.adapter.persistence;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.Admin;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminLog;
import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminLogMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.AdminLogRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.application.request.AdminLogSearchRequest;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.adapter.persistence.JpaPaginationMapper;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.until.StringUtils;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
class AdminLogAdapter implements AdminLogPersistencePort {
    private final AdminLogMapper adminLogMapper;
    private final AdminLogRepository adminLogRepository;

    private Specification<AdminLog> buildSpecification(AdminLogSearchRequest searchRequest) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new LinkedList<Predicate>();

            if (StringUtils.isNotBlank(searchRequest.getAdmin())) {
                var joinAdmin = root.<AdminLog, Admin>join("admin", JoinType.LEFT);

                var adminPredicates = new LinkedList<Predicate>();

                var like = "%" + searchRequest.getAdmin() + "%";

                adminPredicates.add(criteriaBuilder.like(joinAdmin.get("name"), like));
                adminPredicates.add(criteriaBuilder.like(joinAdmin.get("phone"), like));
                adminPredicates.add(criteriaBuilder.like(joinAdmin.get("email"), like));

                predicates.add(criteriaBuilder.or(adminPredicates.toArray(new Predicate[0])));
            }

            if (searchRequest.getType() != null && searchRequest.getType().length > 0) {
                var typePredicates = new LinkedList<Predicate>();

                for (String type : searchRequest.getType()) {
                    typePredicates.add(criteriaBuilder.equal(root.get("type"), type));
                }

                predicates.add(criteriaBuilder.or(typePredicates.toArray(new Predicate[0])));
            }

            if (searchRequest.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), searchRequest.getStatus()));
            }

            var dateTimeRange = searchRequest.getCreatedAtDateTimeRange();

            if (dateTimeRange != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), dateTimeRange.getBegin(), dateTimeRange.getEnd()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public SearchPageResponse<AdminLogEntity> getAll(AdminLogSearchRequest searchRequest, com.huijiewei.agile.core.application.request.PageRequest pageRequest, Boolean withSearchFields) {
        var adminLogPage = this.adminLogRepository.findAll(
                this.buildSpecification(searchRequest),
                PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(Sort.Direction.DESC, "id")),
                DynamicEntityGraph.loading().addPath("admin").build());

        var adminLogEntityResponses = new SearchPageResponse<AdminLogEntity>();

        adminLogEntityResponses.setItems(adminLogPage
                .getContent()
                .stream()
                .map(this.adminLogMapper::toAdminLogEntity)
                .collect(Collectors.toList()));

        adminLogEntityResponses.setPages(JpaPaginationMapper.toPagination(adminLogPage));

        if (withSearchFields != null && withSearchFields) {
            adminLogEntityResponses.setSearchFields(searchRequest.getSearchFields());
        }

        return adminLogEntityResponses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AdminLogEntity adminLogEntity) {
        this.adminLogRepository.save(this.adminLogMapper.toAdminLog(adminLogEntity));
    }
}
