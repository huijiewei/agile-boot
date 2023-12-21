package com.huijiewei.agile.app.admin.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.Admin;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminAccessToken;
import com.huijiewei.agile.core.adapter.persistence.repository.IdentityJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author huijiewei
 */

@Repository
public interface AdminRepository extends
        IdentityJpaRepository<Admin>,
        EntityGraphJpaRepository<Admin, Integer>,
        EntityGraphJpaSpecificationExecutor<Admin> {
    /**
     * 根据 accessToken 和 clientId 获取管理员
     *
     * @param accessToken accessToken
     * @param clientId    clientId
     * @return 管理员
     */
    @Override
    default Optional<Admin> findByAccessToken(String accessToken, String clientId) {
        Specification<Admin> adminSpecification = (root, query, criteriaBuilder) -> {
            var subQuery = query.subquery(Integer.class);
            var subRoot = subQuery.from(AdminAccessToken.class);

            subQuery.select(subRoot.get("adminId"))
                    .where(
                            criteriaBuilder.equal(subRoot.get("accessToken"), accessToken),
                            criteriaBuilder.equal(subRoot.get("clientId"), clientId)
                    );

            return criteriaBuilder.and(root.get("id").in(subQuery));
        };

        return this.findOne(adminSpecification, DynamicEntityGraph.loading().addPath("adminGroup").build());
    }

    /**
     * 判断管理组 Id 是否存在
     *
     * @param adminGroupId 管理组 Id
     * @return Boolean
     */
    boolean existsByAdminGroupId(Integer adminGroupId);
}
