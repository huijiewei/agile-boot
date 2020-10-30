package com.huijiewei.agile.core.adapter.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author huijiewei
 */

@Repository
@RequiredArgsConstructor
public class TreeClosureJpaRepositoryImpl<T extends AbstractJpaTreeClosureEntity> implements TreeClosureJpaRepository<T> {
    private final EntityManager entityManager;

    @Override
    public void truncateClosures(T entity) {
        String tableName = entity.getClosureTableName();

        this.entityManager
                .createNativeQuery(String.format("TRUNCATE TABLE %s", tableName))
                .executeUpdate();
    }

    @Override
    public void insertClosures(T entity) {
        String tableName = entity.getClosureTableName();

        this.entityManager
                .createNativeQuery(String.format("INSERT INTO %s (ancestor, descendant, distance) " +
                        "SELECT ancestor AS ancestor, :id AS descendant, distance + 1 AS distance " +
                        "FROM %s " +
                        "WHERE descendant = :parentId " +
                        "UNION ALL " +
                        "SELECT :id AS ancestor, :id AS descendant, 0 AS distance", tableName, tableName))
                .setParameter("id", entity.getId())
                .setParameter("parentId", entity.getParentId())
                .executeUpdate();
    }

    @Override
    public void updateClosures(T entity) {
        String tableName = entity.getClosureTableName();

        this.entityManager
                .createNativeQuery(String.format("DELETE C FROM %s AS C " +
                        "INNER JOIN %s AS D ON C.descendant = D.descendant " +
                        "LEFT JOIN %s AS A ON A.ancestor = D.ancestor AND A.descendant = C.ancestor " +
                        "WHERE D.ancestor = :id AND A.ancestor = 0", tableName, tableName, tableName))
                .setParameter("id", entity.getId())
                .executeUpdate();

        this.entityManager
                .createNativeQuery(String.format("INSERT INTO %s (ancestor, descendant, distance) " +
                        "SELECT A.ancestor, D.descendant, A.distance + D.distance + 1 " +
                        "FROM %s AS A " +
                        "CROSS JOIN %s AS D " +
                        "WHERE D.ancestor = :id AND A.descendant = :parentId", tableName, tableName, tableName))
                .setParameter("id", entity.getId())
                .setParameter("parentId", entity.getParentId())
                .executeUpdate();
    }

    @Override
    public void deleteWithClosures(T entity) {
        String tableName = entity.getTableName();
        String closureTableName = entity.getClosureTableName();

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

    @Override
    @SuppressWarnings("unchecked")
    public TypedQuery<T> buildAncestorsQuery(T entity) {
        String tableName = entity.getTableName();
        String closureTableName = entity.getClosureTableName();

        return (TypedQuery<T>) this.entityManager
                .createNativeQuery(String.format("SELECT E.* " +
                        "FROM %s AS E " +
                        "INNER JOIN %s AS A ON E.id = A.ancestor " +
                        "WHERE A.descendant = :id " +
                        "ORDER BY E.code, E.id ASC", tableName, closureTableName), entity.getClass())
                .setParameter("id", entity.getId());
    }

    @Override
    public List<T> findDescendants(T entity) {
        return this.buildDescendantsQuery(entity).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public TypedQuery<T> buildDescendantsQuery(T entity) {
        String tableName = entity.getTableName();
        String closureTableName = entity.getClosureTableName();

        return (TypedQuery<T>) this.entityManager
                .createNativeQuery(String.format("SELECT E.* " +
                        "FROM %s AS E " +
                        "INNER JOIN %s AS D ON E.id = D.descendant " +
                        "WHERE D.ancestor = :id " +
                        "ORDER BY E.code, E.id ASC", tableName, closureTableName), entity.getClass())
                .setParameter("id", entity.getId());
    }

    @Override
    public List<T> findAncestorsByKeyword(String keyword, T entity) {
        return this.buildFindAncestorsByKeywordQuery(keyword, entity).getResultList();
    }

    @SuppressWarnings("unchecked")
    private TypedQuery<T> buildFindAncestorsByKeywordQuery(String keyword, T entity) {
        String tableName = entity.getTableName();
        String closureTableName = entity.getClosureTableName();

        return (TypedQuery<T>) this.entityManager
                .createNativeQuery(String.format("SELECT DISTINCT E.* " +
                        "FROM %s AS E " +
                        "INNER JOIN %s AS A ON E.id = A.ancestor " +
                        "INNER JOIN %s AS S ON A.descendant = S.id " +
                        "WHERE S.name LIKE :keyword OR S.code = :code " +
                        "ORDER BY E.CODE, E.id ASC", tableName, closureTableName, tableName), entity.getClass())
                .setParameter("keyword", "%" + keyword + "%")
                .setParameter("code", keyword);
    }
}
