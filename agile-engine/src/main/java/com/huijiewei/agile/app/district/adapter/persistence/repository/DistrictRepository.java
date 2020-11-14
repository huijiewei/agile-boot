package com.huijiewei.agile.app.district.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import com.huijiewei.agile.core.adapter.persistence.repository.TreeClosureJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.OrderBy;
import java.util.List;
import java.util.Map;

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

    default List<District> findAncestorsByKeyword(String keyword) {
        return this.findAncestors("S.name LIKE :keyword OR S.code = :code",
                Map.of("keyword", "%" + keyword + "%", "code", keyword),
                "E.code, E.id ASC",
                District.class);
    }

    default List<District> findAncestorsByCodes(List<String> codes) {
        return this.findAncestors(
                "S.code IN (:codes)",
                Map.of("codes", codes),
                "E.code, E.id ASC",
                District.class);
    }

    default List<District> findDescendantsByKeyword(String keyword) {
        return this.findDescendants("S.name LIKE :keyword OR S.code = :code",
                Map.of("keyword", "%" + keyword + "%", "code", keyword),
                "E.code, E.id ASC",
                District.class);
    }
}
