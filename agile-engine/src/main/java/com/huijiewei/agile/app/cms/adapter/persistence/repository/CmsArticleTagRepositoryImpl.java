package com.huijiewei.agile.app.cms.adapter.persistence.repository;

import com.huijiewei.agile.app.cms.adapter.persistence.entity.CmsArticleTag;
import com.huijiewei.agile.core.adapter.persistence.repository.BatchJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author huijiewei
 */

@Repository
@RequiredArgsConstructor
public class CmsArticleTagRepositoryImpl implements BatchJpaRepository<CmsArticleTag> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(List<CmsArticleTag> entities) {
        if (entities.size() == 0) {
            return;
        }

        jdbcTemplate.batchUpdate(
                String.format("INSERT INTO %s(cmsArticleId,cmsTagId) values(?,?)", CmsArticleTag.tableName(CmsArticleTag.class)),
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NonNull PreparedStatement preparedStatement, int i)
                            throws SQLException {
                        var cmsArticleTag = entities.get(i);

                        preparedStatement.setInt(1, cmsArticleTag.getCmsArticleId());
                        preparedStatement.setInt(2, cmsArticleTag.getCmsTagId());
                    }

                    @Override
                    public int getBatchSize() {
                        return entities.size();
                    }
                });
    }
}
