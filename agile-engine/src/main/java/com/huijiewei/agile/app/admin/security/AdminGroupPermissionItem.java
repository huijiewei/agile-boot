package com.huijiewei.agile.app.admin.security;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@Data
public class AdminGroupPermissionItem {
    private String name;

    private String actionId;

    private List<AdminGroupPermissionItem> children;

    private List<String> combines;

    public AdminGroupPermissionItem name(String name) {
        this.setName(name);

        return this;
    }

    AdminGroupPermissionItem actionId(String actionId) {
        this.setActionId(actionId);

        return this;
    }

    AdminGroupPermissionItem addChild(AdminGroupPermissionItem adminGroupAclItem) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }

        this.children.add(adminGroupAclItem);

        return this;
    }

    AdminGroupPermissionItem addCombine(String combine) {
        if (this.combines == null) {
            this.combines = new ArrayList<>();
        }

        this.combines.add(combine);

        return this;
    }
}
