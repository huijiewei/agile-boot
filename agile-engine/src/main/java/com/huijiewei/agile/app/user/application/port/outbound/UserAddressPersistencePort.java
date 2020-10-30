package com.huijiewei.agile.app.user.application.port.outbound;

import com.huijiewei.agile.app.user.application.request.UserAddressSearchRequest;
import com.huijiewei.agile.app.user.domain.UserAddressEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface UserAddressPersistencePort {
    SearchPageResponse<UserAddressEntity> getAll(Integer page, Integer size, UserAddressSearchRequest searchRequest, Boolean withSearchFields);

    List<UserAddressEntity> getAllByUserId(Integer userId);

    Optional<UserAddressEntity> getById(Integer id);

    Integer save(UserAddressEntity userAddressEntity);

    void deleteById(Integer id);
}
