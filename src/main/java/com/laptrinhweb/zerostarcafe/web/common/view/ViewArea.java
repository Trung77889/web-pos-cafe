package com.laptrinhweb.zerostarcafe.web.common.view;

/**
 * Defines the UI areas supported by the application (client, admin, shared).
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/12/2025
 * @since 1.0.0
 */
public enum ViewArea {
    CLIENT, ADMIN, SHARED;

    public static ViewArea detectArea(String path) {
        if (path.startsWith("/admin/"))
            return ADMIN;

        return CLIENT;
    }
}