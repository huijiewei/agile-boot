package com.huijiewei.agile.app.admin.adapter.persistence;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminLog;
import com.huijiewei.agile.app.admin.adapter.persistence.mapper.AdminLogMapper;
import com.huijiewei.agile.app.admin.adapter.persistence.repository.JpaAdminLogRepository;
import com.huijiewei.agile.app.admin.application.port.outbound.AdminLogPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminLogEntity;
import com.huijiewei.agile.core.adapter.persistence.PaginationCover;
import com.huijiewei.agile.core.application.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
class JpaAdminLogAdapter implements AdminLogPersistencePort {
    private final AdminLogMapper adminLogMapper;
    private final JpaAdminLogRepository jpaAdminLogRepository;

    @Autowired
    public JpaAdminLogAdapter(AdminLogMapper adminLogMapper, JpaAdminLogRepository jpaAdminLogRepository) {
        this.adminLogMapper = adminLogMapper;
        this.jpaAdminLogRepository = jpaAdminLogRepository;
    }

    @Override
    public PageResponse<AdminLogEntity> getAll(Integer page, Integer size) {
        Page<AdminLog> adminLogPage = this.jpaAdminLogRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC));

        PageResponse<AdminLogEntity> adminLogEntityPageResponse = new PageResponse<>();

        adminLogEntityPageResponse.setItems(adminLogPage
                .getContent()
                .stream()
                .map(this.adminLogMapper::toAdminLogEntity)
                .collect(Collectors.toList()));

        adminLogEntityPageResponse.setPages(PaginationCover.toPagination(adminLogPage));

        return adminLogEntityPageResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AdminLogEntity adminLogEntity) {
        this.jpaAdminLogRepository.save(this.adminLogMapper.toAdminLog(adminLogEntity));
    }
}
