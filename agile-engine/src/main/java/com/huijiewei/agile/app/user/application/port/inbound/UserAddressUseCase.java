package com.huijiewei.agile.app.user.application.port.inbound;

import com.huijiewei.agile.app.user.application.request.UserAddressRequest;
import com.huijiewei.agile.app.user.application.request.UserAddressSearchRequest;
import com.huijiewei.agile.app.user.domain.UserAddressEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

/**
 * @author huijiewei
 */

public interface UserAddressUseCase {
    SearchPageResponse<UserAddressEntity> all(Integer page, Integer size, UserAddressSearchRequest searchRequest, Boolean withSearchFields);

    UserAddressEntity read(Integer id);

    UserAddressEntity create(UserAddressRequest userRequest);

    UserAddressEntity update(Integer id, UserAddressRequest userRequest);

    void deleteById(Integer id);
}
