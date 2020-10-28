package com.huijiewei.agile.core.adapter.persistence;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author huijiewei
 */

public class JpaClosureRepositoryImpl<T extends AbstractJpaTreeClosureEntity> implements JpaClosureRepository<T> {
    private final EntityManager entityManager;

    public JpaClosureRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void truncateClosures(T entity) {
        String tableName = entity.getClosureTableName();

        this.entityManager
                .createNativeQuery(String.format("TRUNCATE TABLE %s", tableName))
                .executeUpdate();
    }

    @Override
    @Transactional
    public void insertClosures(T entity) {
        String tableName = entity.getClosureTableName();

        this.entityManager
                .createNativeQuery(String.format("INSERT INTO %s (ancestor, descendant, distance)\n" +
                        "SELECT ancestor AS ancestor, :id AS descendant, distance + 1 AS distance\n" +
                        "FROM %s\n" +
                        "WHERE descendant = :parentId\n" +
                        "UNION ALL\n" +
                        "SELECT :id AS ancestor, :id AS descendant, 0 AS distance", tableName, tableName))
                .setParameter("id", entity.getId())
                .setParameter("parentId", entity.getParentId())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void updateClosures(T entity) {
        String tableName = entity.getClosureTableName();

        this.entityManager.createNativeQuery(String.format("DELETE FROM closures USING %s AS closures\n" +
                "INNER JOIN %s AS descendants\n" +
                "   ON closures.descendant = descendants.descendant\n" +
                "INNER JOIN %s AS ancestors\n" +
                "   ON closures.ancestor = ancestors.ancestor\n" +
                "WHERE descendants.ancestor = :id\n" +
                "   AND ancestors.descendant = :id\n" +
                "   AND closures.distance > descendants.distance", tableName, tableName, tableName))
                .setParameter("id", entity.getId())
                .executeUpdate();

        this.entityManager.createNativeQuery(String.format("INSERT INTO %s (ancestor, descendant, distance)\n" +
                "SELECT ancestors.ancestor, descendants.descendant, ancestors.distance + descendants.distance + 1\n" +
                "FROM %s AS ancestors\n" +
                "CROSS JOIN %s AS descendants\n" +
                "WHERE ancestors.descendant = :parentId\n" +
                "   AND descendants.ancestor = :id", tableName, tableName, tableName))
                .setParameter("id", entity.getId())
                .setParameter("parentId", entity.getParentId())
                .executeUpdate();
    }
}
