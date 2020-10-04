package com.huijiewei.agile.app.user.application.service;

import com.huijiewei.agile.app.user.application.port.outbound.UserUniquePort;
import com.huijiewei.agile.core.application.port.inbound.UniqueUseCase;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author huijiewei
 */

@Service
public class UserUniqueService implements UniqueUseCase {
    private final UserUniquePort userUniquePort;

    public UserUniqueService(UserUniquePort userUniquePort) {
        this.userUniquePort = userUniquePort;
    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.userUniquePort.unique(values, primaryKey, primaryValue);
    }
}
