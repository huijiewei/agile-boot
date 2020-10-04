package com.huijiewei.agile.app.user.application.port.inbound;

import com.huijiewei.agile.app.user.application.request.UserRequest;
import com.huijiewei.agile.app.user.application.request.UserSearchRequest;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.app.user.domain.UserEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author huijiewei
 */

public interface UserUseCase {
    SearchPageResponse<UserEntity> all(Integer page, Integer size, UserSearchRequest searchRequest, Boolean withSearchFields);

    void export(UserSearchRequest searchRequest, OutputStream outputStream) throws IOException;

    UserEntity read(Integer id);

    UserEntity create(UserRequest userRequest, UserCreatedFrom createdFrom, String createdIp);

    UserEntity update(Integer id, UserRequest userRequest);

    void deleteById(Integer id);
}
