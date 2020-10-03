package com.huijiewei.agile.app.admin.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.Admin;
import com.huijiewei.agile.app.admin.adapter.persistence.entity.AdminAccessToken;
import com.huijiewei.agile.core.adapter.persistence.JpaIdentityRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Optional;

/**
 * @author huijiewei
 */

@Repository
public interface JpaAdminRepository extends
        JpaIdentityRepository<Admin>,
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
        Specification<Admin> adminSpecification = (Specification<Admin>) (root, query, criteriaBuilder) -> {
            Subquery<Integer> subQuery = query.subquery(Integer.class);
            Root<AdminAccessToken> subRoot = subQuery.from(AdminAccessToken.class);

            subQuery.select(subRoot.get("adminId"))
                    .where(
                            criteriaBuilder.equal(subRoot.get("accessToken"), accessToken),
                            criteriaBuilder.equal(subRoot.get("clientId"), clientId)
                    );

            return criteriaBuilder.and(root.get("id").in(subQuery));
        };

        return this.findOne(adminSpecification, EntityGraphUtils.fromAttributePaths("adminGroup"));
    }

    /**
     * 判断管理组 Id 是否存在
     *
     * @param adminGroupId 管理组 Id
     * @return Boolean
     */
    Boolean existsByAdminGroupId(Integer adminGroupId);
}
