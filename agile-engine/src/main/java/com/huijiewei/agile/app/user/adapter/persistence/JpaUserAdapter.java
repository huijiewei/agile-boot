package com.huijiewei.agile.app.user.adapter.persistence;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.huijiewei.agile.app.user.adapter.persistence.entity.User;
import com.huijiewei.agile.app.user.adapter.persistence.mapper.UserMapper;
import com.huijiewei.agile.app.user.adapter.persistence.repository.JpaUserRepository;
import com.huijiewei.agile.app.user.application.port.outbound.UserPersistencePort;
import com.huijiewei.agile.app.user.application.port.outbound.UserUniquePort;
import com.huijiewei.agile.app.user.application.request.UserSearchRequest;
import com.huijiewei.agile.app.user.domain.UserEntity;
import com.huijiewei.agile.core.until.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

public class JpaUserAdapter implements UserUniquePort, UserPersistencePort {
    private final UserMapper userMapper;
    private final JpaUserRepository userRepository;

    public JpaUserAdapter(UserMapper userMapper, JpaUserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> getAll(UserSearchRequest searchRequest) {
        PredicateBuilder<User> predicateBuilder = Specifications.<User>and()
                .like(StringUtils.isNotEmpty(searchRequest.getName()), "name", '%' + searchRequest.getName() + '%')
                .like(StringUtils.isNotEmpty(searchRequest.getPhone()), "phone", '%' + searchRequest.getPhone() + '%')
                .like(StringUtils.isNotEmpty(searchRequest.getEmail()), "email", '%' + searchRequest.getEmail() + '%');

        if (searchRequest.getCreatedFrom() != null && searchRequest.getCreatedFrom().length > 0) {
            PredicateBuilder<User> createdFromPredicateBuilder = Specifications.or();

            for (String createdFrom : searchRequest.getCreatedFrom()) {
                createdFromPredicateBuilder.eq(StringUtils.isNotEmpty(createdFrom), "createdFrom", createdFrom);
            }

            predicateBuilder.predicate(createdFromPredicateBuilder.build());
        }

        LocalDateTime[] createdRanges = DateTimeUtils.parseSearchDateRange(searchRequest.getCreatedRange());

        if (createdRanges != null) {
            predicateBuilder.between("createdAt", createdRanges[0], createdRanges[1]);
        }

        return this.userRepository.findAll(predicateBuilder.build()).stream().map(this.userMapper::toUserEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<UserEntity> getById(Integer id) {
        return this.userRepository.findById(id).map(this.userMapper::toUserEntity);
    }

    @Override
    public Integer save(UserEntity userEntity) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        Specification<User> userSpecification = (Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            for (Map.Entry<String, String> entry : values.entrySet()) {
                predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
            }

            if (StringUtils.isNotEmpty(primaryValue)) {
                predicates.add(criteriaBuilder.notEqual(root.get(primaryKey), primaryValue));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return this.userRepository.count(userSpecification) == 0;
    }
}
