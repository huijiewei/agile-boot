package com.huijiewei.agile.app.admin.application.port.outbound;

import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.app.admin.security.AdminGroupMenuItem;

import java.util.List;
import java.util.Optional;

/**
 * @author huijiewei
 */

public interface AdminGroupPersistencePort {
    List<AdminGroupEntity> getAll();

    Optional<AdminGroupEntity> getById(Integer id);

    Integer save(AdminGroupEntity adminGroupEntity);

    void deleteById(Integer id);

    List<String> getPermissions(Integer id);

    void updatePermissions(Integer id, List<String> permissions, Boolean delete);

    List<AdminGroupMenuItem> getMenus(Integer id);
}
