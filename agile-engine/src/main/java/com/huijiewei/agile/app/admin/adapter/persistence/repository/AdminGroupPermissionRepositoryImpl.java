package com.huijiewei.agile.app.admin.adapter.persistence.repository;

import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminGroupPermission;
import com.huijiewei.agile.core.adapter.persistence.repository.BatchJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author huijiewei
 */

@RequiredArgsConstructor
public class AdminGroupPermissionRepositoryImpl implements BatchJpaRepository<AdminGroupPermission> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(List<AdminGroupPermission> adminGroupPermissions) {
        if (adminGroupPermissions.size() == 0) {
            return;
        }

        jdbcTemplate.batchUpdate(
                String.format("INSERT INTO %s(adminGroupId,actionId) values(?,?)", AdminGroupPermission.tableName(AdminGroupPermission.class)),
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i)
                            throws SQLException {
                        AdminGroupPermission adminGroupPermission = adminGroupPermissions.get(i);

                        preparedStatement.setInt(1, adminGroupPermission.getAdminGroupId());
                        preparedStatement.setString(2, adminGroupPermission.getActionId());
                    }

                    @Override
                    public int getBatchSize() {
                        return adminGroupPermissions.size();
                    }
                });
    }
}
