package com.huijiewei.agile.app.admin.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huijiewei
 */

public interface JpaAdminLogRepository extends
        JpaRepository<AdminLog, Integer>,
        EntityGraphJpaSpecificationExecutor<AdminLog> {
}
