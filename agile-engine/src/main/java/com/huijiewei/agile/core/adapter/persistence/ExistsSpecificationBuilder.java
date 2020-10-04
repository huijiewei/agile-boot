package com.huijiewei.agile.core.adapter.persistence;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

/**
 * @author huijiewei
 */

public class ExistsSpecificationBuilder {
    public static <T extends AbstractJpaEntity> Specification<T> build(String targetProperty, List<String> values) {
        return (Specification<T>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            for (String value : values) {
                predicates.add(criteriaBuilder.equal(root.get(targetProperty), value));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
