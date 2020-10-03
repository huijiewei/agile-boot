package com.huijiewei.agile.core.adapter.persistence;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author huijiewei
 */

@NoRepositoryBean
public interface BatchRepository<T extends AbstractJpaEntity> {
    /**
     * 批量插入方法
     *
     * @param entities 实例列表
     */
    void batchInsert(List<T> entities);
}
