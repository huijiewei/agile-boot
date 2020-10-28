package com.huijiewei.agile.core.adapter.persistence;

/**
 * @author huijiewei
 */

public interface JpaClosureRepository<T extends AbstractJpaTreeClosureEntity> {
    void truncateClosures(T entity);

    void insertClosures(T entity);

    void updateClosures(T entity);
}
