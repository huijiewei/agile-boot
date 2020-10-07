package com.huijiewei.agile.app.district.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.district.adapter.persistence.entity.District;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

@Repository
public interface DistrictJpaRepository extends
        EntityGraphJpaRepository<District, Integer>,
        EntityGraphJpaSpecificationExecutor<District> {
    @Modifying
    @Query("DELETE FROM District WHERE id IN ?1")
    void deleteAllById(List<Integer> ids);

    /**
     * 获取地区列表
     *
     * @param parentId parentId
     * @return 地区列表
     */
    @Query("FROM District WHERE parentId = ?1 ORDER BY code, id ASC")
    List<District> findByParentId(Integer parentId);

    /**
     * 获取地区和他上级
     *
     * @param id id
     * @return 地区
     */
    @Query("FROM District D " +
            "LEFT JOIN FETCH D.parent P1 " +
            "LEFT JOIN FETCH P1.parent P2 " +
            "LEFT JOIN FETCH P2.parent P3 " +
            "WHERE D.id = ?1 " +
            "ORDER BY D.code, D.id ASC")
    Optional<District> findByIdWithParents(Integer id);

    @Query("FROM District WHERE parentId = ?1 " +
            "OR " +
            "id IN (" +
            "SELECT D2.id FROM District AS D1, District AS D2 " +
            "WHERE D1.parentId = ?1 AND D2.parentId = D1.id) " +
            "OR " +
            "id IN (" +
            "SELECT D3.id FROM District AS D1, District AS D2, District AS D3 " +
            "WHERE D1.parentId = ?1 AND D2.parentId = D1.id AND D3.parentId = D2.id)")
    List<District> findChildrenById(Integer id);

    /**
     * 获取地区和他上级
     *
     * @param keyword keyword
     * @return 地区列表
     */
    @Query("FROM District D " +
            "LEFT JOIN FETCH D.parent P1 " +
            "LEFT JOIN FETCH P1.parent P2 " +
            "LEFT JOIN FETCH P2.parent P3 " +
            "WHERE (D.name LIKE CONCAT('%', ?1, '%') OR D.code = ?1) " +
            "ORDER BY D.code, D.id ASC")
    List<District> findByKeywordWithParents(String keyword);
}
