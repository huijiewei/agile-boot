package com.huijiewei.agile.app.admin.adapter.persistence.repository;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author huijiewei
 */

public interface JpaAdminLogRepository extends
        JpaRepository<AdminLog, Integer>,
        JpaSpecificationExecutor<AdminLog> {
}
