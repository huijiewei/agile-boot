package com.huijiewei.agile.core.adapter.persistence;

import com.huijiewei.agile.core.adapter.persistence.entity.AbstractJpaEntity;
import com.huijiewei.agile.core.until.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author huijiewei
 */

public class JpaSpecificationBuilder {
    public static <T extends AbstractJpaEntity> Specification<T> buildUnique(Map<String, String> values, String primaryKey, String primaryValue) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new LinkedList<Predicate>();

            for (var entry : values.entrySet()) {
                predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
            }

            if (StringUtils.isNotEmpty(primaryValue)) {
                predicates.add(criteriaBuilder.notEqual(root.get(primaryKey), primaryValue));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T extends AbstractJpaEntity> Specification<T> buildExists(String targetProperty, List<String> values) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new LinkedList<Predicate>();

            for (var value : values) {
                predicates.add(criteriaBuilder.equal(root.get(targetProperty), value));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
