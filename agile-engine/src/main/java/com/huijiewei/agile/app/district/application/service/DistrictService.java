package com.huijiewei.agile.app.district.application.service;

import com.huijiewei.agile.app.district.application.mapper.DistrictRequestMapper;
import com.huijiewei.agile.app.district.application.port.inbound.DistrictUseCase;
import com.huijiewei.agile.app.district.application.port.outbound.DistrictPersistencePort;
import com.huijiewei.agile.app.district.application.request.DistrictRequest;
import com.huijiewei.agile.app.district.domain.DistrictEntity;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huijiewei
 */

@Service
public class DistrictService implements DistrictUseCase {
    private final DistrictPersistencePort districtPersistencePort;
    private final ValidatingService validatingService;
    private final DistrictRequestMapper districtRequestMapper;

    public DistrictService(DistrictPersistencePort districtPersistencePort, ValidatingService validatingService, DistrictRequestMapper districtRequestMapper) {
        this.districtPersistencePort = districtPersistencePort;
        this.validatingService = validatingService;
        this.districtRequestMapper = districtRequestMapper;
    }

    private DistrictEntity getById(Integer id) {
        return this.districtPersistencePort.getById(id).orElseThrow(() -> new NotFoundException("地区不存在"));
    }

    @Override
    public List<DistrictEntity> getAllByParentId(Integer parentId) {
        return this.districtPersistencePort.getAllByParentId(parentId);
    }

    @Override
    public List<DistrictEntity> getPath(Integer id) {
        return this.districtPersistencePort.getParentByIdWithParents(id);
    }

    @Override
    public List<DistrictEntity> getTreeByKeyword(String keyword) {
        return this.districtPersistencePort.getTreeByKeyword(keyword);
    }

    @Override
    public DistrictEntity read(Integer id, Boolean withParents) {
        DistrictEntity districtEntity = this.getById(id);

        if (withParents != null && withParents && districtEntity.getParentId() > 0) {
            districtEntity.setParents(this.districtPersistencePort.getParentByIdWithParents(districtEntity.getParentId()));
        }

        return districtEntity;
    }

    @Override
    public DistrictEntity create(DistrictRequest districtRequest) {
        if (!this.validatingService.validate(districtRequest)) {
            return null;
        }

        DistrictEntity districtEntity = this.districtRequestMapper.toDistrictEntity(districtRequest);

        if (!this.validatingService.validate(districtEntity)) {
            return null;
        }

        Integer districtId = this.districtPersistencePort.save(districtEntity);
        districtEntity.setId(districtId);

        return districtEntity;
    }

    @Override
    public DistrictEntity update(Integer id, DistrictRequest districtRequest) {
        DistrictEntity districtEntity = this.getById(id);

        if (!this.validatingService.validate(districtRequest)) {
            return null;
        }

        this.districtRequestMapper.updateDistrictEntity(districtRequest, districtEntity);

        if (!this.validatingService.validate(districtEntity)) {
            return null;
        }

        Integer districtId = this.districtPersistencePort.save(districtEntity);
        districtEntity.setId(districtId);

        return districtEntity;
    }

    @Override
    public void deleteById(Integer id) {
        DistrictEntity currentDistrictEntity = this.getById(id);

        this.districtPersistencePort.deleteById(currentDistrictEntity.getId());
    }
}
