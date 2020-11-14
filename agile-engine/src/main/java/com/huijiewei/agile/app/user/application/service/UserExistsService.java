package com.huijiewei.agile.app.user.application.service;

import com.huijiewei.agile.app.user.application.port.outbound.UserExistsPort;
import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class UserExistsService implements ExistsUseCase {
    private final UserExistsPort userExistsPort;

    @Override
    public boolean exists(String targetProperty, List<String> values) {
        return this.userExistsPort.exists(targetProperty, values);
    }
}
