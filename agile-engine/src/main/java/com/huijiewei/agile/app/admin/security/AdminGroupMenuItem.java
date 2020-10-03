package com.huijiewei.agile.app.admin.security;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huijiewei
 */

@Data
public class AdminGroupMenuItem {
    private String label;

    private String icon;

    private String url;

    private Boolean open = false;

    private List<AdminGroupMenuItem> children;

    AdminGroupMenuItem label(String label) {
        this.setLabel(label);

        return this;
    }

    AdminGroupMenuItem icon(String icon) {
        this.setIcon(icon);

        return this;
    }

    AdminGroupMenuItem url(String url) {
        this.setUrl(url);

        return this;
    }

    AdminGroupMenuItem open() {
        this.setOpen(true);

        return this;
    }

    AdminGroupMenuItem addChild(AdminGroupMenuItem adminGroupMenuItem) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }

        this.children.add(adminGroupMenuItem);

        return this;
    }
}
