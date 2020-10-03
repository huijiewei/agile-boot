package com.huijiewei.agile.app.admin.adapter.persistence.repository;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author huijiewei
 */

@Repository
public interface JpaAdminGroupRepository extends
        JpaRepository<AdminGroup, Integer>,
        JpaSpecificationExecutor<AdminGroup> {
}
