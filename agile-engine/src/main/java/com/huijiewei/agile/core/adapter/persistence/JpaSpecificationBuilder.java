package com.huijiewei.agile.core.adapter.persistence;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author huijiewei
 */

public class JpaSpecificationBuilder {
    public static <T extends AbstractJpaEntity> Specification<T> buildUnique(Map<String, String> values, String primaryKey, String primaryValue) {
        return (Specification<T>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            for (Map.Entry<String, String> entry : values.entrySet()) {
                predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
            }

            if (StringUtils.isNotEmpty(primaryValue)) {
                predicates.add(criteriaBuilder.notEqual(root.get(primaryKey), primaryValue));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T extends AbstractJpaEntity> Specification<T> buildExists(String targetProperty, List<String> values) {
        return (Specification<T>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            for (String value : values) {
                predicates.add(criteriaBuilder.equal(root.get(targetProperty), value));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
