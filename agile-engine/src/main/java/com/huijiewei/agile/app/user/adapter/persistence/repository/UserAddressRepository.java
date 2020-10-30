package com.huijiewei.agile.app.user.adapter.persistence.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import com.huijiewei.agile.app.user.adapter.persistence.entity.UserAddress;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huijiewei
 */

@Repository
public interface UserAddressRepository extends
        EntityGraphJpaRepository<UserAddress, Integer>,
        EntityGraphJpaSpecificationExecutor<UserAddress> {
    List<UserAddress> findAllByUserId(Integer userId);
}
