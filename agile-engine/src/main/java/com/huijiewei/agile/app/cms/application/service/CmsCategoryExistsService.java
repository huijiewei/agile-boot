package com.huijiewei.agile.app.cms.application.service;

import com.huijiewei.agile.app.cms.application.port.outbound.CmsCategoryExistsPort;
import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
@RequiredArgsConstructor
public class CmsCategoryExistsService implements ExistsUseCase {
    private final CmsCategoryExistsPort cmsCategoryExistsPort;

    @Override
    public boolean exists(String targetProperty, List<String> values) {
        return this.cmsCategoryExistsPort.exists(targetProperty, values);
    }
}
