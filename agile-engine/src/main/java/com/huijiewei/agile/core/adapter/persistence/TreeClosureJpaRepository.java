package com.huijiewei.agile.core.adapter.persistence;

import com.github.pnowy.nc.core.expressions.NativeOrderExp;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * @author huijiewei
 */

public interface TreeClosureJpaRepository<T extends AbstractJpaTreeClosureEntity> {
    void truncateClosures(T entity);

    void insertClosures(T entity);

    void updateClosures(T entity);

    void deleteWithClosures(T entity);

    List<T> findAncestors(T entity);

    TypedQuery<T> buildAncestorsQuery(T entity);

    List<T> findDescendants(T entity);

    TypedQuery<T> buildDescendantsQuery(T entity);

    List<T> findAncestors(String sql, Map<String, Object> values, Map<String, NativeOrderExp.OrderType> orders, T entity);
}
