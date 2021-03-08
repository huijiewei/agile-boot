package com.huijiewei.agile.app.user.application.service;

import com.huijiewei.agile.app.user.application.mapper.UserRequestMapper;
import com.huijiewei.agile.app.user.application.port.inbound.UserUseCase;
import com.huijiewei.agile.app.user.application.port.outbound.UserExportPort;
import com.huijiewei.agile.app.user.application.port.outbound.UserPersistencePort;
import com.huijiewei.agile.app.user.application.request.UserRequest;
import com.huijiewei.agile.app.user.application.request.UserSearchRequest;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.app.user.domain.UserEntity;
import com.huijiewei.agile.core.application.request.PageRequest;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.NotFoundException;
import com.huijiewei.agile.core.until.SecurityUtils;
import com.huijiewei.agile.core.until.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {
    private final UserPersistencePort userPersistencePort;
    private final ValidatingService validatingService;
    private final UserRequestMapper userRequestMapper;
    private final UserExportPort userExportPort;

    @Override
    public SearchPageResponse<UserEntity> search(UserSearchRequest searchRequest, PageRequest pageRequest, Boolean withSearchFields) {
        return this.userPersistencePort.getAll(searchRequest, pageRequest, withSearchFields);
    }

    @Override
    public void export(UserSearchRequest searchRequest, OutputStream outputStream) throws IOException {
        this.userExportPort.export(this.userPersistencePort.getAll(searchRequest), outputStream);
    }

    private UserEntity getById(Integer id) {
        return this.userPersistencePort.getById(id)
                .orElseThrow(() -> new NotFoundException("用户不存在"));
    }

    @Override
    public UserEntity loadById(Integer id) {
        return this.getById(id);
    }

    @Override
    public UserEntity create(UserRequest userRequest, UserCreatedFrom createdFrom, String createdIp) {
        if (!this.validatingService.validate(userRequest, UserRequest.OnCreate.class)) {
            return null;
        }

        var userEntity = this.userRequestMapper.toUserEntity(userRequest);
        userEntity.setPassword(SecurityUtils.passwordEncode(userRequest.getPassword()));

        if (!this.validatingService.validate(userEntity)) {
            return null;
        }

        userEntity.setCreatedIp(createdIp);
        userEntity.setCreatedFrom(createdFrom);

        var userId = this.userPersistencePort.save(userEntity);
        userEntity.setId(userId);

        return userEntity;
    }

    @Override
    public UserEntity update(Integer id, UserRequest userRequest) {
        var userEntity = this.getById(id);

        if (!this.validatingService.validate(userRequest, UserRequest.OnUpdate.class)) {
            return null;
        }

        this.userRequestMapper.updateUserEntity(userRequest, userEntity);

        if (StringUtils.isNotEmpty(userRequest.getPassword())) {
            userEntity.setPassword(SecurityUtils.passwordEncode(userRequest.getPassword()));
        }

        if (!this.validatingService.validate(userEntity)) {
            return null;
        }

        Integer userId = this.userPersistencePort.save(userEntity);
        userEntity.setId(userId);

        return userEntity;
    }

    @Override
    public void deleteById(Integer id) {
        this.userPersistencePort.deleteById(this.getById(id).getId());
    }
}
