package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupExistsPort;
import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class AdminGroupExistsService implements ExistsUseCase {
    private final AdminGroupExistsPort adminGroupExistsPort;

    @Override
    public Boolean exists(String targetProperty, List<String> values) {
        return this.adminGroupExistsPort.exists(targetProperty, values);
    }
}
