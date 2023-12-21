package com.huijiewei.agile.core.adapter.persistence.repository;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaEntity;
import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaTreeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Map;

/**
 * @author huijiewei
 */

@Repository
@RequiredArgsConstructor
public class TreeClosureJpaRepositoryImpl<T extends AbstractJpaTreeEntity> implements TreeClosureJpaRepository<T> {
    private final EntityManager entityManager;

    private String getEntityTableName(T entity) {
        return AbstractJpaEntity.tableName(entity.getClass());
    }

    private String getEntityTableName(Class<T> entityType) {
        return AbstractJpaEntity.tableName(entityType);
    }

    private String getEntityClosureTableName(T entity) {
        return this.getEntityClosureTableName(this.getEntityTableName(entity));
    }

    private String getEntityClosureTableName(Class<T> entityType) {
        return this.getEntityClosureTableName(this.getEntityTableName(entityType));
    }

    private String getEntityClosureTableName(String entityTableName) {
        return entityTableName + "_closure";
    }

    @Override
    public void truncateClosures(Class<T> entityType) {
        var closureTableName = this.getEntityClosureTableName(entityType);

        this.entityManager
                .createNativeQuery(String.format("TRUNCATE TABLE %s", closureTableName))
                .executeUpdate();
    }

    @Override
    public void insertClosures(T entity) {
        var closureTableName = this.getEntityClosureTableName(entity);

        this.entityManager
                .createNativeQuery(String.format("INSERT INTO %s (ancestor, descendant, distance) " +
                        "SELECT ancestor AS ancestor, :id AS descendant, distance + 1 AS distance " +
                        "FROM %s " +
                        "WHERE descendant = :parentId " +
                        "UNION ALL " +
                        "SELECT :id AS ancestor, :id AS descendant, 0 AS distance", closureTableName, closureTableName))
                .setParameter("id", entity.getId())
                .setParameter("parentId", entity.getParentId())
                .executeUpdate();
    }

    @Override
    public void updateClosures(T entity) {
        var closureTableName = this.getEntityClosureTableName(entity);

        this.entityManager
                .createNativeQuery(String.format("DELETE C FROM %s AS C " +
                        "INNER JOIN %s AS D ON C.descendant = D.descendant " +
                        "LEFT JOIN %s AS A ON A.ancestor = D.ancestor AND A.descendant = C.ancestor " +
                        "WHERE D.ancestor = :id AND A.ancestor IS NULL", closureTableName, closureTableName, closureTableName))
                .setParameter("id", entity.getId())
                .executeUpdate();

        this.entityManager
                .createNativeQuery(String.format("INSERT INTO %s (ancestor, descendant, distance) " +
                        "SELECT A.ancestor, D.descendant, A.distance + D.distance + 1 " +
                        "FROM %s AS A " +
                        "CROSS JOIN %s AS D " +
                        "WHERE D.ancestor = :id AND A.descendant = :parentId", closureTableName, closureTableName, closureTableName))
                .setParameter("id", entity.getId())
                .setParameter("parentId", entity.getParentId())
                .executeUpdate();
    }

    @Override
    public void deleteWithClosures(T entity) {
        var tableName = this.getEntityTableName(entity);
        var closureTableName = this.getEntityClosureTableName(tableName);

        this.entityManager
                .createNativeQuery(String.format("DELETE D, DC " +
                        "FROM %s C " +
                        "INNER JOIN %s D ON C.descendant = D.id " +
                        "INNER JOIN %s DC ON DC.ancestor = D.id OR DC.descendant = D.id " +
                        "WHERE C.ancestor = :id", closureTableName, tableName, closureTableName))
                .setParameter("id", entity.getId())
                .executeUpdate();
    }

    @Override
    public List<T> findAncestors(T entity) {
        return this.buildAncestorsQuery(entity).getResultList();
    }

    @SuppressWarnings("unchecked")
    private TypedQuery<T> buildAncestorsQuery(T entity) {
        var tableName = this.getEntityTableName(entity);
        var closureTableName = this.getEntityClosureTableName(tableName);

        return (TypedQuery<T>) this.entityManager
                .createNativeQuery(String.format("SELECT E.* " +
                        "FROM %s AS E " +
                        "INNER JOIN %s AS A ON E.id = A.ancestor " +
                        "WHERE A.descendant = :id " +
                        "ORDER BY E.id ASC", tableName, closureTableName), entity.getClass())
                .setParameter("id", entity.getId());
    }

    @Override
    public List<T> findDescendants(T entity) {
        return this.buildDescendantsQuery(entity).getResultList();
    }

    @SuppressWarnings("unchecked")
    private TypedQuery<T> buildDescendantsQuery(T entity) {
        var tableName = this.getEntityTableName(entity);
        var closureTableName = this.getEntityClosureTableName(tableName);

        return (TypedQuery<T>) this.entityManager
                .createNativeQuery(String.format("SELECT E.* " +
                        "FROM %s AS E " +
                        "INNER JOIN %s AS D ON E.id = D.descendant " +
                        "WHERE D.ancestor = :id " +
                        "ORDER BY E.id ASC", tableName, closureTableName), entity.getClass())
                .setParameter("id", entity.getId());
    }

    @Override
    public List<T> findAncestors(String where, Map<String, Object> values, String sort, Class<T> entityType) {
        return this.buildAncestorsQuery(where, values, sort, entityType).getResultList();
    }

    @SuppressWarnings("unchecked")
    private TypedQuery<T> buildAncestorsQuery(String where, Map<String, Object> values, String sort, Class<T> entityType) {
        var tableName = this.getEntityTableName(entityType);
        var closureTableName = this.getEntityClosureTableName(tableName);

        var query = (TypedQuery<T>) this.entityManager
                .createNativeQuery(String.format("SELECT DISTINCT E.* " +
                        "FROM %s AS E " +
                        "INNER JOIN %s AS A ON E.id = A.ancestor " +
                        "INNER JOIN %s AS S ON A.descendant = S.id " +
                        "WHERE %s " +
                        "ORDER BY %s", tableName, closureTableName, tableName, where, sort), entityType);

        for (var value : values.entrySet()) {
            query.setParameter(value.getKey(), value.getValue());
        }

        return query;
    }

    @Override
    public List<T> findDescendants(String where, Map<String, Object> values, String sort, Class<T> entityType) {
        return this.buildDescendantsQuery(where, values, sort, entityType).getResultList();
    }

    @SuppressWarnings("unchecked")
    private TypedQuery<T> buildDescendantsQuery(String where, Map<String, Object> values, String sort, Class<T> entityType) {
        var tableName = this.getEntityTableName(entityType);
        var closureTableName = this.getEntityClosureTableName(tableName);

        var query = (TypedQuery<T>) this.entityManager
                .createNativeQuery(String.format("SELECT DISTINCT E.* " +
                        "FROM %s AS E " +
                        "INNER JOIN %s AS D ON E.id = D.descendant " +
                        "INNER JOIN %s AS S ON D.ancestor = S.id " +
                        "WHERE %s " +
                        "ORDER BY %s", tableName, closureTableName, tableName, where, sort), entityType);

        for (var value : values.entrySet()) {
            query.setParameter(value.getKey(), value.getValue());
        }

        return query;
    }
}
