package com.huijiewei.agile.app.user.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.user.adapter.persistence.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author huijiewei
 */

@Repository
public interface JpaUserRepository extends
        EntityGraphJpaRepository<User, Integer>,
        EntityGraphJpaSpecificationExecutor<User> {
}
