package com.huijiewei.agile.app.district.application.service;

import com.huijiewei.agile.app.district.application.port.outbound.DistrictExistsPort;
import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
public class DistrictExistsService implements ExistsUseCase {
    private final DistrictExistsPort districtExistsPort;

    public DistrictExistsService(DistrictExistsPort districtExistsPort) {
        this.districtExistsPort = districtExistsPort;
    }

    @Override
    public Boolean exists(String targetProperty, List<String> values) {
        return this.districtExistsPort.exists(targetProperty, values);
    }
}
