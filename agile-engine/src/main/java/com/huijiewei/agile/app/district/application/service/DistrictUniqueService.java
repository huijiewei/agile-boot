package com.huijiewei.agile.app.district.application.service;

import com.huijiewei.agile.app.district.application.port.outbound.DistrictUniquePort;
import com.huijiewei.agile.core.application.port.inbound.UniqueUseCase;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author huijiewei
 */

@Service
public class DistrictUniqueService implements UniqueUseCase {
    private final DistrictUniquePort districtUniquePort;

    public DistrictUniqueService(DistrictUniquePort districtUniquePort) {
        this.districtUniquePort = districtUniquePort;
    }

    @Override
    public boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.districtUniquePort.unique(values, primaryKey, primaryValue);
    }
}
