package com.huijiewei.agile.app.district.application.service;

import com.huijiewei.agile.app.district.application.mapper.DistrictRequestMapper;
import com.huijiewei.agile.app.district.application.port.inbound.DistrictUseCase;
import com.huijiewei.agile.app.district.application.port.outbound.DistrictPersistencePort;
import com.huijiewei.agile.app.district.application.request.DistrictRequest;
import com.huijiewei.agile.app.district.domain.DistrictEntity;
import com.huijiewei.agile.core.application.service.ValidatingService;
import com.huijiewei.agile.core.exception.ConflictException;
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
    public List<DistrictEntity> loadByParentId(Integer parentId) {
        return this.districtPersistencePort.getAllByParentId(parentId);
    }

    @Override
    public List<DistrictEntity> loadPathById(Integer id) {
        return this.districtPersistencePort.getAncestorsById(id);
    }

    @Override
    public List<DistrictEntity> loadTreeByKeyword(String keyword) {
        return this.districtPersistencePort.getAncestorsTreeByKeyword(keyword);
    }

    @Override
    public DistrictEntity loadById(Integer id, Boolean withParents) {
        var districtEntity = this.getById(id);

        if (withParents != null && withParents && districtEntity.getParentId() > 0) {
            districtEntity.setParents(this.districtPersistencePort.getAncestorsById(districtEntity.getParentId()));
        }

        return districtEntity;
    }

    @Override
    public DistrictEntity create(DistrictRequest districtRequest) {
        if (!this.validatingService.validate(districtRequest)) {
            return null;
        }

        if (this.checkParentDistrictIsLeaf(districtRequest.getParentId())) {
            throw new ConflictException("选择的上级地区不允许添加下级地区");
        }

        var districtEntity = this.districtRequestMapper.toDistrictEntity(districtRequest);

        if (!this.validatingService.validate(districtEntity)) {
            return null;
        }

        var districtId = this.districtPersistencePort.save(districtEntity);
        districtEntity.setId(districtId);

        return districtEntity;
    }

    private Boolean checkParentDistrictIsLeaf(Integer parentId) {
        if (parentId == 0) {
            return false;
        }

        return this.getById(parentId).isLeaf();
    }

    @Override
    public DistrictEntity update(Integer id, DistrictRequest districtRequest) {
        var districtEntity = this.getById(id);

        if (!this.validatingService.validate(districtRequest)) {
            return null;
        }

        if (this.checkParentDistrictIsLeaf(districtRequest.getParentId())) {
            throw new ConflictException("选择的上级地区不允许添加下级地区");
        }

        this.districtRequestMapper.updateDistrictEntity(districtRequest, districtEntity);

        if (!this.validatingService.validate(districtEntity)) {
            return null;
        }

        var districtId = this.districtPersistencePort.save(districtEntity);
        districtEntity.setId(districtId);

        return districtEntity;
    }

    @Override
    public void deleteById(Integer id) {
        var districtEntity = this.getById(id);

        this.districtPersistencePort.delete(districtEntity);
    }
}
