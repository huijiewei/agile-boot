package com.huijiewei.agile.app.shop.adapter.persistence.repository;

import com.huijiewei.agile.app.shop.adapter.persistence.entity.ShopBrandCategory;
import com.huijiewei.agile.core.adapter.persistence.BatchJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author huijiewei
 */

@Repository
@RequiredArgsConstructor
public class ShopBrandCategoryRepositoryImpl implements BatchJpaRepository<ShopBrandCategory> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(List<ShopBrandCategory> entities) {
        if (entities.size() == 0) {
            return;
        }

        jdbcTemplate.batchUpdate(
                String.format("INSERT INTO %s(shopBrandId,shopCategoryId) values(?,?)", ShopBrandCategory.tableName(ShopBrandCategory.class)),
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i)
                            throws SQLException {
                        ShopBrandCategory shopBrandCategory = entities.get(i);

                        preparedStatement.setInt(1, shopBrandCategory.getShopBrandId());
                        preparedStatement.setInt(2, shopBrandCategory.getShopCategoryId());
                    }

                    @Override
                    public int getBatchSize() {
                        return entities.size();
                    }
                });
    }
}
