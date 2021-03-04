package com.huijiewei.agile.app.user.application.port.outbound;

import com.huijiewei.agile.app.user.application.request.UserSearchRequest;
import com.huijiewei.agile.app.user.domain.UserEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.SearchPageResponse;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface UserPersistencePort {
    SearchPageResponse<UserEntity> getAll(UserSearchRequest searchRequest, PageRequest pageRequest, Boolean withSearchFields);

    List<UserEntity> getAll(UserSearchRequest searchRequest);

    Optional<UserEntity> getById(Integer id);

    Integer save(UserEntity userEntity);

    void deleteById(Integer id);
}
