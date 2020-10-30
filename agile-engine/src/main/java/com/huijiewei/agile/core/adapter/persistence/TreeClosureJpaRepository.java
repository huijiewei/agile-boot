package com.huijiewei.agile.core.adapter.persistence;

import javax.persistence.TypedQuery;
import java.util.List;

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

    List<T> findAncestorsByKeyword(String keyword, T entity);
}
