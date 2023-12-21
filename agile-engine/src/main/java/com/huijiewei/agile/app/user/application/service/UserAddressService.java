package com.huijiewei.agile.app.user.application.service;

import com.huijiewei.agile.app.district.application.port.outbound.DistrictPersistencePort;
import com.huijiewei.agile.app.user.application.mapper.UserAddressRequestMapper;
import com.huijiewei.agile.app.user.application.port.inbound.UserAddressUseCase;
import com.huijiewei.agile.app.user.application.port.outbound.UserAddressPersistencePort;
import com.huijiewei.agile.app.user.application.request.UserAddressRequest;
import com.huijiewei.agile.app.user.application.request.UserAddressSearchRequest;
import com.huijiewei.agile.app.user.domain.UserAddressEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class UserAddressService implements UserAddressUseCase {
    private final UserAddressPersistencePort userAddressPersistencePort;
    private final DistrictPersistencePort districtPersistencePort;
    private final ValidatingService validatingService;
    private final UserAddressRequestMapper userAddressRequestMapper;

    private UserAddressEntity fillDistrictPath(UserAddressEntity userAddressEntity) {
        return this.fillDistrictPath(List.of(userAddressEntity)).getFirst();
    }

    private List<UserAddressEntity> fillDistrictPath(List<UserAddressEntity> userAddressEntities) {
        var districtCodes = userAddressEntities
                .stream()
                .map(UserAddressEntity::getDistrictCode)
                .distinct()
                .collect(Collectors.toList());

        var districtMap = this.districtPersistencePort.getAllByCodesWithParents(districtCodes);

        for (var userAddressEntity : userAddressEntities) {
            userAddressEntity.setDistrictPath(districtMap.get(userAddressEntity.getDistrictCode()));
        }

        return userAddressEntities;
    }

    @Override
    public SearchPageResponse<UserAddressEntity> search(UserAddressSearchRequest searchRequest, PageRequest pageRequest, Boolean withSearchFields) {
        var userAddressEntitySearchPageResponse = this.userAddressPersistencePort.getAll(searchRequest, pageRequest, withSearchFields);

        userAddressEntitySearchPageResponse.setItems(this.fillDistrictPath(userAddressEntitySearchPageResponse.getItems()));

        return userAddressEntitySearchPageResponse;
    }

    private UserAddressEntity getById(Integer id) {
        return this.userAddressPersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("用户地址不存在"));
    }

    @Override
    public UserAddressEntity loadById(Integer id) {
        return this.fillDistrictPath(this.getById(id));
    }

    @Override
    public UserAddressEntity create(UserAddressRequest userAddressRequest) {
        if (!this.validatingService.validate(userAddressRequest)) {
            return null;
        }

        var userAddressEntity = this.userAddressRequestMapper.toUserAddressEntity(userAddressRequest);

        var userAddressId = this.userAddressPersistencePort.save(userAddressEntity);
        userAddressEntity.setId(userAddressId);

        return this.fillDistrictPath(userAddressEntity);
    }

    @Override
    public UserAddressEntity update(Integer id, UserAddressRequest userAddressRequest) {
        var userAddressEntity = this.getById(id);

        if (!this.validatingService.validate(userAddressRequest)) {
            return null;
        }

        this.userAddressRequestMapper.updateUserAddressEntity(userAddressRequest, userAddressEntity);

        var userAddressId = this.userAddressPersistencePort.save(userAddressEntity);
        userAddressEntity.setId(userAddressId);

        return this.fillDistrictPath(userAddressEntity);
    }

    @Override
    public void deleteById(Integer id) {
        this.userAddressPersistencePort.deleteById(this.getById(id).getId());
    }
}
