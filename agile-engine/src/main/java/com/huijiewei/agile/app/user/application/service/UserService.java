package com.huijiewei.agile.app.user.application.service;

import com.huijiewei.agile.app.user.application.mapper.UserRequestMapper;
import com.huijiewei.agile.app.user.application.port.inbound.UserUseCase;
import com.huijiewei.agile.app.user.application.port.outbound.UserExportPort;
import com.huijiewei.agile.app.user.application.port.outbound.UserPersistencePort;
import com.huijiewei.agile.app.user.application.request.UserRequest;
import com.huijiewei.agile.app.user.application.request.UserSearchRequest;
import com.huijiewei.agile.app.user.consts.UserCreatedFrom;
import com.huijiewei.agile.app.user.domain.UserEntity;
import com.huijiewei.agile.core.application.response.SearchPageResponse;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.NotFoundException;
import com.huijiewei.agile.core.until.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author huijiewei
 */

@Service
public class UserService implements UserUseCase {
    private final UserPersistencePort userPersistencePort;
    private final ValidatingService validatingService;
    private final UserRequestMapper userRequestMapper;
    private final UserExportPort userExportPort;

    public UserService(UserPersistencePort userPersistencePort, ValidatingService validatingService, UserRequestMapper userRequestMapper, UserExportPort userExportPort) {
        this.userPersistencePort = userPersistencePort;
        this.validatingService = validatingService;
        this.userRequestMapper = userRequestMapper;
        this.userExportPort = userExportPort;
    }

    @Override
    public SearchPageResponse<UserEntity> all(Integer page, Integer size, UserSearchRequest searchRequest, Boolean withSearchFields) {
        return this.userPersistencePort.getAll(page, size, searchRequest, withSearchFields);
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
    public UserEntity read(Integer id) {
        return this.getById(id);
    }

    @Override
    public UserEntity create(UserRequest userRequest, UserCreatedFrom createdFrom, String createdIp) {
        if (!this.validatingService.validate(userRequest, UserRequest.OnCreate.class)) {
            return null;
        }

        UserEntity userEntity = this.userRequestMapper.toUserEntity(userRequest);
        userEntity.setPassword(SecurityUtils.passwordEncode(userRequest.getPassword()));

        if (!this.validatingService.validate(userEntity)) {
            return null;
        }

        userEntity.setCreatedIp(createdIp);
        userEntity.setCreatedFrom(createdFrom);

        Integer userId = this.userPersistencePort.save(userEntity);
        userEntity.setId(userId);

        return userEntity;
    }

    @Override
    public UserEntity update(Integer id, UserRequest userRequest) {
        if (!this.validatingService.validate(userRequest, UserRequest.OnUpdate.class)) {
            return null;
        }

        UserEntity currentUserEntity = this.getById(id);
        UserEntity userEntity = this.userRequestMapper.toUserEntity(userRequest, currentUserEntity);

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
