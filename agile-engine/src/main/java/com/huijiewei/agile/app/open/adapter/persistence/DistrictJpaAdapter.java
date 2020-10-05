package com.huijiewei.agile.app.open.adapter.persistence;

import com.huijiewei.agile.app.open.adapter.persistence.entity.District;
import com.huijiewei.agile.app.open.adapter.persistence.mapper.DistrictMapper;
import com.huijiewei.agile.app.open.adapter.persistence.repository.DistrictJpaRepository;
import com.huijiewei.agile.app.open.application.port.outbound.DistrictExistsPort;
import com.huijiewei.agile.app.open.application.port.outbound.DistrictPersistencePort;
import com.huijiewei.agile.app.open.application.port.outbound.DistrictUniquePort;
import com.huijiewei.agile.app.open.domain.DistrictEntity;
import com.huijiewei.agile.core.adapter.persistence.ExistsSpecificationBuilder;
import com.huijiewei.agile.core.adapter.persistence.UniqueSpecificationBuilder;
import com.huijiewei.agile.core.until.TreeUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
public class DistrictJpaAdapter implements DistrictExistsPort, DistrictPersistencePort, DistrictUniquePort {
    private final DistrictJpaRepository districtJpaRepository;
    private final DistrictMapper districtMapper;

    public DistrictJpaAdapter(DistrictJpaRepository districtJpaRepository, DistrictMapper districtMapper) {
        this.districtJpaRepository = districtJpaRepository;
        this.districtMapper = districtMapper;
    }

    @Override
    public Boolean exists(String targetProperty, List<String> values) {
        return this.districtJpaRepository.count(ExistsSpecificationBuilder.build(targetProperty, values)) > 0;
    }

    @Override
    public Optional<DistrictEntity> getById(Integer id) {
        return this.districtJpaRepository.findById(id).map(this.districtMapper::toDistrictEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(DistrictEntity districtEntity) {
        District district = this.districtJpaRepository.save(this.districtMapper.toDistrict(districtEntity));

        return district.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.districtJpaRepository.deleteById(id);
        this.districtJpaRepository.deleteByParentId(id);
    }

    @Override
    public List<DistrictEntity> getAllByParentId(Integer parentId) {
        return this.districtJpaRepository
                .findAllByParentId(parentId)
                .stream()
                .map(this.districtMapper::toDistrictEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DistrictEntity> getParentByIdWithParents(Integer id) {
        List<DistrictEntity> parents = new ArrayList<>();

        Optional<District> parentOptional = this.districtJpaRepository.findParentByIdWithParents(id);

        if (parentOptional.isPresent()) {
            District parent = parentOptional.get();
            parents.add(this.districtMapper.toDistrictEntity(parent));

            if (parent.getParentId() > 0 && parent.getParent() != null) {
                parents.add(this.districtMapper.toDistrictEntity(parent.getParent()));

                if (parent.getParent().getParentId() > 0 && parent.getParent().getParent() != null) {
                    parents.add(this.districtMapper.toDistrictEntity(parent.getParent().getParent()));
                }
            }
        }

        if (parents.size() > 0) {
            Collections.reverse(parents);
        }

        return parents;
    }

    @Override
    public List<DistrictEntity> getTreeByKeyword(String keyword) {
        List<District> districts = this.districtJpaRepository.findAllByKeywordWithParents(keyword);

        List<DistrictEntity> districtEntities = new ArrayList<>();
        List<Integer> resultRepeatCheckIds = new ArrayList<>();

        for (District district : districts) {
            if (!resultRepeatCheckIds.contains(district.getId())) {
                districtEntities.add(this.districtMapper.toDistrictEntity(district));
                resultRepeatCheckIds.add(district.getId());
            }

            if (district.getParentId() > 0 && district.getParent() != null) {
                if (!resultRepeatCheckIds.contains(district.getParent().getId())) {
                    districtEntities.add(this.districtMapper.toDistrictEntity(district.getParent()));
                    resultRepeatCheckIds.add(district.getParent().getId());
                }

                if (district.getParent().getParentId() > 0 && district.getParent().getParent() != null) {
                    if (!resultRepeatCheckIds.contains(district.getParent().getParent().getId())) {
                        districtEntities.add(this.districtMapper.toDistrictEntity(district.getParent().getParent()));
                        resultRepeatCheckIds.add(district.getParent().getParent().getId());
                    }

                    if (district.getParent().getParent().getParentId() > 0 && district.getParent().getParent().getParent() != null) {
                        if (!resultRepeatCheckIds.contains(district.getParent().getParent().getParent().getId())) {
                            districtEntities.add(this.districtMapper.toDistrictEntity(district.getParent().getParent().getParent()));
                            resultRepeatCheckIds.add(district.getParent().getParent().getParent().getId());
                        }
                    }
                }
            }
        }

        return TreeUtils.buildTree(districtEntities);
    }

    @Override
    public Boolean unique(Map<String, String> values, String primaryKey, String primaryValue) {
        return this.districtJpaRepository.count(UniqueSpecificationBuilder.build(values, primaryKey, primaryValue)) == 0;
    }
}
