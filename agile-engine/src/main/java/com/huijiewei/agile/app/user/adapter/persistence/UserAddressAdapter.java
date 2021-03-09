package com.huijiewei.agile.app.user.adapter.persistence;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.app.district.adapter.persistence.repository.DistrictRepository;
import com.huijiewei.agile.app.user.adapter.persistence.entity.UserAddress;
import com.huijiewei.agile.app.user.adapter.persistence.mapper.UserAddressMapper;
import com.huijiewei.agile.app.user.adapter.persistence.repository.UserAddressRepository;
import com.huijiewei.agile.app.user.application.port.outbound.UserAddressPersistencePort;
import com.huijiewei.agile.app.user.application.request.UserAddressSearchRequest;
import com.huijiewei.agile.app.user.domain.UserAddressEntity;
import com.huijiewei.agile.core.adapter.persistence.JpaPaginationMapper;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.until.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserAddressAdapter implements UserAddressPersistencePort {
    private final UserAddressRepository userAddressRepository;
    private final UserAddressMapper userAddressMapper;
    private final DistrictRepository districtRepository;

    private Specification<UserAddress> buildSpecification(UserAddressSearchRequest searchRequest) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new LinkedList<Predicate>();

            if (StringUtils.isNotBlank(searchRequest.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchRequest.getName() + "%"));
            }

            if (StringUtils.isNotBlank(searchRequest.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + searchRequest.getPhone() + "%"));
            }

            if (StringUtils.isNotBlank(searchRequest.getUserName())) {
                predicates.add(criteriaBuilder.like(root.get("user").get("name"), "%" + searchRequest.getUserName() + "%"));
            }

            if (StringUtils.isNotBlank(searchRequest.getUserEmail())) {
                predicates.add(criteriaBuilder.like(root.get("user").get("email"), "%" + searchRequest.getUserEmail() + "%"));
            }

            if (StringUtils.isNotBlank(searchRequest.getUserPhone())) {
                predicates.add(criteriaBuilder.like(root.get("user").get("phone"), "%" + searchRequest.getUserPhone() + "%"));
            }

            if (StringUtils.isNotBlank(searchRequest.getDistrictName())) {
                predicates.add(root.get("districtCode").in(this.districtRepository
                        .findDescendantsByKeyword(searchRequest.getDistrictName())
                        .stream()
                        .map(District::getCode)
                        .collect(Collectors.toList())
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public SearchPageResponse<UserAddressEntity> getAll(UserAddressSearchRequest searchRequest, com.huijiewei.agile.core.application.request.PageRequest pageRequest, Boolean withSearchFields) {
        var userAddressPage = this.userAddressRepository.findAll(
                this.buildSpecification(searchRequest),
                PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(Sort.Direction.DESC, "id")),
                EntityGraphUtils.fromAttributePaths("user")
        );

        var userAddressEntityResponse = new SearchPageResponse<UserAddressEntity>();

        userAddressEntityResponse.setItems(userAddressPage
                .getContent()
                .stream()
                .map(this.userAddressMapper::toUserAddressEntity)
                .collect(Collectors.toList()));

        userAddressEntityResponse.setPages(JpaPaginationMapper.toPagination(userAddressPage));

        if (withSearchFields != null && withSearchFields) {
            userAddressEntityResponse.setSearchFields(searchRequest.getSearchFields());
        }

        return userAddressEntityResponse;
    }

    @Override
    public List<UserAddressEntity> getAllByUserId(Integer userId) {
        return this.userAddressRepository.findByUserId(userId)
                .stream()
                .map(this.userAddressMapper::toUserAddressEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserAddressEntity> getById(Integer id) {
        return this.userAddressRepository
                .findById(id)
                .map(this.userAddressMapper::toUserAddressEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(UserAddressEntity userAddressEntity) {
        var userAddress = this.userAddressRepository.save(this.userAddressMapper.toUserAddress(userAddressEntity));

        return userAddress.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.userAddressRepository.deleteById(id);
    }
}
