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
    @Query("DELETE FROM District WHERE parentId = ?1")
    void deleteByParentId(Integer parentId);

    /**
     * 获取地区列表
     *
     * @param parentId parentId
     * @return 地区列表
     */
    @Query("FROM District WHERE parentId = ?1 ORDER BY code, id ASC")
    List<District> findAllByParentId(Integer parentId);

    /**
     * 获取上级地区和他上级
     *
     * @param id id
     * @return 地区
     */
    @Query("FROM District P1 " +
            "LEFT JOIN FETCH P1.parent P2 " +
            "LEFT JOIN FETCH P2.parent P3 " +
            "WHERE P1.id=?1 " +
            "AND (P2.id is NULL OR P2.id=P1.parentId) " +
            "AND (P3.id is NULL OR P3.id=P2.parentId) " +
            "ORDER BY P1.code, P1.id ASC")
    Optional<District> findParentByIdWithParents(Integer id);

    /**
     * 获取地区和他上级
     *
     * @param keyword keyword
     * @return 地区
     */
    @Query("FROM District D " +
            "LEFT JOIN FETCH D.parent P1 " +
            "LEFT JOIN FETCH P1.parent P2 " +
            "LEFT JOIN FETCH P2.parent P3 " +
            "WHERE (LOCATE(?1, D.name) > 0 OR D.code = ?1) " +
            "AND (P1.id is NULL OR P1.id=D.parentId) " +
            "AND (P2.id is NULL OR P2.id=P1.parentId) " +
            "AND (P3.id is NULL OR P3.id=P2.parentId) " +
            "ORDER BY D.code, D.id ASC")
    List<District> findAllByKeywordWithParents(String keyword);
}
