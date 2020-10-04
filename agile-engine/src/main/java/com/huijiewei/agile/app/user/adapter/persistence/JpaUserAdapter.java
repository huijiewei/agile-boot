package com.huijiewei.agile.app.user.adapter.persistence;

import com.huijiewei.agile.app.user.adapter.persistence.entity.User;
import com.huijiewei.agile.app.user.adapter.persistence.mapper.UserMapper;
import com.huijiewei.agile.app.user.adapter.persistence.repository.JpaUserRepository;
import com.huijiewei.agile.app.user.application.port.outbound.UserPersistencePort;
import com.huijiewei.agile.app.user.application.port.outbound.UserUniquePort;
import com.huijiewei.agile.app.user.application.request.UserSearchRequest;
import com.huijiewei.agile.app.user.domain.UserEntity;
import com.huijiewei.agile.core.adapter.persistence.PaginationCover;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.until.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

@Component
public class JpaUserAdapter implements UserUniquePort, UserPersistencePort {
    private final UserMapper userMapper;
    private final JpaUserRepository userRepository;

    public JpaUserAdapter(UserMapper userMapper, JpaUserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    private Specification<User> buildSpecification(UserSearchRequest searchRequest) {
        return (Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (StringUtils.isNotBlank(searchRequest.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchRequest.getName() + "%"));
            }

            if (StringUtils.isNotBlank(searchRequest.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + searchRequest.getEmail() + "%"));
            }

            if (StringUtils.isNotBlank(searchRequest.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + searchRequest.getPhone() + "%"));
            }

            if (searchRequest.getCreatedFrom() != null && searchRequest.getCreatedFrom().length > 0) {
                List<Predicate> createdFromPredicates = new LinkedList<>();

                for (String createdFrom : searchRequest.getCreatedFrom()) {
                    createdFromPredicates.add(criteriaBuilder.equal(root.get("createdFrom"), createdFrom));
                }

                predicates.add(criteriaBuilder.or(createdFromPredicates.toArray(new Predicate[0])));
            }

            LocalDateTime[] createdRanges = DateTimeUtils.parseSearchDateRange(searchRequest.getCreatedRange());

            if (createdRanges != null) {
                predicates.add(criteriaBuilder.between(root.get("createdAt"), createdRanges[0], createdRanges[1]));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public SearchPageResponse<UserEntity> getAll(Integer page, Integer size, UserSearchRequest searchRequest, Boolean withSearchFields) {
        Page<User> userPage = this.userRepository.findAll(
                this.buildSpecification(searchRequest),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        );

        SearchPageResponse<UserEntity> userEntityResponses = new SearchPageResponse<>();

        userEntityResponses.setItems(userPage
                .getContent()
                .stream()
                .map(this.userMapper::toUserEntity)
                .collect(Collectors.toList()));

        userEntityResponses.setPages(PaginationCover.toPagination(userPage));

        if (withSearchFields != null && withSearchFields) {
            userEntityResponses.setSearchFields(searchRequest.getSearchFields());
        }

        return userEntityResponses;
    }

    @Override
    public List<UserEntity> getAll(UserSearchRequest searchRequest) {
        return this.userRepository
                .findAll(this.buildSpecification(searchRequest))
                .stream()
                .map(this.userMapper::toUserEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserEntity> getById(Integer id) {
        return this.userRepository.findById(id).map(this.userMapper::toUserEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(UserEntity userEntity) {
        User user = this.userRepository.save(this.userMapper.toUser(userEntity));

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.userRepository.deleteById(id);
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
