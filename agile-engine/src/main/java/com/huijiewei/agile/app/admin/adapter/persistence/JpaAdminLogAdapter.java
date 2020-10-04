package com.huijiewei.agile.app.admin.adapter.persistence;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminLog;
import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminLogMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.JpaAdminLogRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.application.request.AdminLogSearchRequest;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.adapter.persistence.PaginationCover;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.until.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
class JpaAdminLogAdapter implements AdminLogPersistencePort {
    private final AdminLogMapper adminLogMapper;
    private final JpaAdminLogRepository jpaAdminLogRepository;

    public JpaAdminLogAdapter(AdminLogMapper adminLogMapper, JpaAdminLogRepository jpaAdminLogRepository) {
        this.adminLogMapper = adminLogMapper;
        this.jpaAdminLogRepository = jpaAdminLogRepository;
    }

    @Override
    public SearchPageResponse<AdminLogEntity> getAll(Integer page, Integer size, AdminLogSearchRequest searchRequest, Boolean withSearchFields) {
        PredicateBuilder<AdminLog> predicateBuilder = Specifications.<AdminLog>and()
                .predicate(
                        StringUtils.isNotEmpty(searchRequest.getAdmin()),
                        Specifications.or()
                                .like("admin.name", '%' + searchRequest.getAdmin() + '%')
                                .like("admin.phone", '%' + searchRequest.getAdmin() + '%')
                                .like("admin.email", '%' + searchRequest.getAdmin() + '%')
                                .build()
                );

        if (searchRequest.getType() != null && searchRequest.getType().length > 0) {
            PredicateBuilder<AdminLog> createdFromPredicateBuilder = Specifications.or();

            for (String type : searchRequest.getType()) {
                createdFromPredicateBuilder.eq(StringUtils.isNotEmpty(type), "type", type);
            }

            predicateBuilder.predicate(createdFromPredicateBuilder.build());
        }
        if (searchRequest.getStatus() != null) {
            predicateBuilder.eq("status", searchRequest.getStatus());
        }

        LocalDateTime[] createdRanges = DateTimeUtils.parseSearchDateRange(searchRequest.getCreatedRange());

        if (createdRanges != null) {
            predicateBuilder.between("createdAt", createdRanges[0], createdRanges[1]);
        }

        Page<AdminLog> adminLogPage = this.jpaAdminLogRepository.findAll(predicateBuilder.build(), PageRequest.of(page, size, Sorts.builder().desc("id").build()));

        SearchPageResponse<AdminLogEntity> adminLogEntityResponses = new SearchPageResponse<>();

        adminLogEntityResponses.setItems(adminLogPage
                .getContent()
                .stream()
                .map(this.adminLogMapper::toAdminLogEntity)
                .collect(Collectors.toList()));

        adminLogEntityResponses.setPages(PaginationCover.toPagination(adminLogPage));

        if (withSearchFields != null && withSearchFields) {
            adminLogEntityResponses.setSearchFields(searchRequest.getSearchFields());
        }

        return adminLogEntityResponses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AdminLogEntity adminLogEntity) {
        this.jpaAdminLogRepository.save(this.adminLogMapper.toAdminLog(adminLogEntity));
    }
}
