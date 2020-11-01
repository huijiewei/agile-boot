package com.huijiewei.agile.app.district.adapter.persistence;

import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.app.district.adapter.persistence.mapper.DistrictMapper;
import com.huijiewei.agile.app.district.adapter.persistence.repository.DistrictRepository;
import com.huijiewei.agile.app.district.application.port.outbound.DistrictExistsPort;
import com.huijiewei.agile.app.district.application.port.outbound.DistrictPersistencePort;
import com.huijiewei.agile.app.district.application.port.outbound.DistrictUniquePort;
import com.huijiewei.agile.app.district.domain.DistrictEntity;
import com.huijiewei.agile.core.adapter.persistence.JpaSpecificationBuilder;
import com.huijiewei.agile.core.until.TreeUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
public class DistrictAdapter implements DistrictExistsPort, DistrictPersistencePort, DistrictUniquePort {
    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

    public DistrictAdapter(DistrictRepository districtRepository, DistrictMapper districtMapper) {
        this.districtRepository = districtRepository;
        this.districtMapper = districtMapper;
    }

    @Override
    public Boolean exists(String targetProperty, List<String> values) {
        return this.districtRepository.count(JpaSpecificationBuilder.buildExists(targetProperty, values)) > 0;
    }

    @Override
    public Optional<DistrictEntity> getById(Integer id) {
        return this.districtRepository.findById(id).map(this.districtMapper::toDistrictEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(DistrictEntity districtEntity) {
        District district = this.districtRepository.save(this.districtMapper.toDistrict(districtEntity));

        if (districtEntity.hasId()) {
            this.districtRepository.updateClosures(district);
        } else {
            this.districtRepository.insertClosures(district);
        }

        return district.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(DistrictEntity districtEntity) {
        this.districtRepository.deleteWithClosures(this.districtMapper.toDistrict(districtEntity));
    }

    @Override
    public List<DistrictEntity> getAllByParentId(Integer parentId) {
        return this.districtRepository
                .findByParentId(parentId)
                .stream()
                .map(this.districtMapper::toDistrictEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DistrictEntity> getAncestorsById(Integer id) {
        District district = new District();
        district.setId(id);

        return this.districtRepository.findAncestors(district)
                .stream()
                .map(this.districtMapper::toDistrictEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DistrictEntity> getAncestorsByKeyword(String keyword) {
        return this.districtRepository
                .findAncestorsByKeyword(keyword)
                .stream()
                .map(this.districtMapper::toDistrictEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DistrictEntity> getAncestorsTreeByKeyword(String keyword) {
        return TreeUtils.buildTree(this.getAncestorsByKeyword(keyword));
    }

    @Override
    public Map<String, List<DistrictEntity>> getAllByCodesWithParents(List<String> codes) {
        Map<String, List<DistrictEntity>> districtMap = new HashMap<>(codes.size());

        List<DistrictEntity> districts = this.districtRepository
                .findAncestorsByCodes(codes)
                .stream()
                .map(this.districtMapper::toDistrictEntity)
                .collect(Collectors.toList());

        for (String code : codes) {
            for (DistrictEntity district : districts) {
                if (district.getCode().equals(code)) {
                    districtMap.put(code, TreeUtils.getParents(district.getId(), districts));
                }
            }
        }

        return districtMap;
    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.districtRepository.count(JpaSpecificationBuilder.buildUnique(values, primaryKey, primaryValue)) == 0;
    }
}
