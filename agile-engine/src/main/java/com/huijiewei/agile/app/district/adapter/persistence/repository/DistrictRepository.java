package com.huijiewei.agile.app.district.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.core.adapter.persistence.TreeClosureJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.OrderBy;
import java.util.List;

/**
 * @author huijiewei
 */

@Repository
public interface DistrictRepository extends
        EntityGraphJpaRepository<District, Integer>,
        EntityGraphJpaSpecificationExecutor<District>,
        TreeClosureJpaRepository<District> {
    /**
     * 获取地区列表
     *
     * @param parentId parentId
     * @return 地区列表
     */
    @OrderBy("code, id ASC")
    List<District> findByParentId(Integer parentId);
}
