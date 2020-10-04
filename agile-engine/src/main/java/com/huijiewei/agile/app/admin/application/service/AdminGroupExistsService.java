package com.huijiewei.agile.app.admin.application.service;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupExistsPort;
import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
public class AdminGroupExistsService implements ExistsUseCase {
    private final AdminGroupExistsPort adminGroupExistsPort;

    public AdminGroupExistsService(AdminGroupExistsPort adminGroupExistsPort) {
        this.adminGroupExistsPort = adminGroupExistsPort;
    }

    @Override
    public Boolean exists(String targetProperty, List<String> values) {
        return this.adminGroupExistsPort.exist(targetProperty, values);
    }
}
