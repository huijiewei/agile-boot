package com.huijiewei.agile.core.adapter.persistence.repository;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaTreeEntity;

import java.util.List;
import java.util.Map;

/**
 * @author huijiewei
 */

public interface TreeClosureJpaRepository<T extends AbstractJpaTreeEntity> {
    void truncateClosures(Class<T> entityType);

    void insertClosures(T entity);

    void updateClosures(T entity);

    void deleteWithClosures(T entity);

    List<T> findAncestors(T entity);

    List<T> findDescendants(T entity);

    List<T> findAncestors(String where, Map<String, Object> values, String sort, Class<T> entityType);

    List<T> findDescendants(String where, Map<String, Object> values, String sort, Class<T> entityType);
}
