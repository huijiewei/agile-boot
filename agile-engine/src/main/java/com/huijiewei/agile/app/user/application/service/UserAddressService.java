package com.huijiewei.agile.app.user.application.service;

import com.huijiewei.agile.app.district.application.port.outbound.DistrictPersistencePort;
import com.huijiewei.agile.app.district.domain.DistrictEntity;
import com.huijiewei.agile.app.user.application.mapper.UserAddressRequestMapper;
import com.huijiewei.agile.app.user.application.port.inbound.UserAddressUseCase;
import com.huijiewei.agile.app.user.application.port.outbound.UserAddressPersistencePort;
import com.huijiewei.agile.app.user.application.request.UserAddressRequest;
import com.huijiewei.agile.app.user.application.request.UserAddressSearchRequest;
import com.huijiewei.agile.app.user.domain.UserAddressEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
        return this.fillDistrictPath(List.of(userAddressEntity)).get(0);
    }

    private List<UserAddressEntity> fillDistrictPath(List<UserAddressEntity> userAddressEntities) {
        List<String> districtCodes = userAddressEntities
                .stream()
                .map(UserAddressEntity::getDistrictCode)
                .distinct()
                .collect(Collectors.toList());

        Map<String, List<DistrictEntity>> districtMap = this.districtPersistencePort.getAllByCodesWithParents(districtCodes);

        for (UserAddressEntity userAddressEntity : userAddressEntities) {
            userAddressEntity.setDistrictPath(districtMap.get(userAddressEntity.getDistrictCode()));
        }

        return userAddressEntities;
    }

    @Override
    public SearchPageResponse<UserAddressEntity> all(Integer page, Integer size, UserAddressSearchRequest searchRequest, Boolean withSearchFields) {
        SearchPageResponse<UserAddressEntity> userAddressEntitySearchPageResponse = this.userAddressPersistencePort.getAll(page, size, searchRequest, withSearchFields);

        userAddressEntitySearchPageResponse.setItems(this.fillDistrictPath(userAddressEntitySearchPageResponse.getItems()));

        return userAddressEntitySearchPageResponse;
    }

    private UserAddressEntity getById(Integer id) {
        return this.userAddressPersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("用户地址不存在"));
    }

    @Override
    public UserAddressEntity read(Integer id) {
        return this.fillDistrictPath(this.getById(id));
    }

    @Override
    public UserAddressEntity create(UserAddressRequest userAddressRequest) {
        if (!this.validatingService.validate(userAddressRequest)) {
            return null;
        }

        UserAddressEntity userAddressEntity = this.userAddressRequestMapper.toUserAddressEntity(userAddressRequest);

        Integer userAddressId = this.userAddressPersistencePort.save(userAddressEntity);
        userAddressEntity.setId(userAddressId);

        return this.fillDistrictPath(userAddressEntity);
    }

    @Override
    public UserAddressEntity update(Integer id, UserAddressRequest userAddressRequest) {
        UserAddressEntity userAddressEntity = this.getById(id);

        if (!this.validatingService.validate(userAddressRequest)) {
            return null;
        }

        this.userAddressRequestMapper.updateUserAddressEntity(userAddressRequest, userAddressEntity);

        Integer userAddressId = this.userAddressPersistencePort.save(userAddressEntity);
        userAddressEntity.setId(userAddressId);

        return this.fillDistrictPath(userAddressEntity);
    }

    @Override
    public void deleteById(Integer id) {
        this.userAddressPersistencePort.deleteById(this.getById(id).getId());
    }
}
