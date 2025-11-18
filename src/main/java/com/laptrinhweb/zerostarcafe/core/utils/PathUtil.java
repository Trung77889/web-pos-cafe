package com.laptrinhweb.zerostarcafe.core.utils;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility class providing centralized JSP view paths for all web areas
 * (client, admin, manager, staff). This helps avoid hard-coded strings
 * and keeps view structure consistent across the application.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * String page = PathUtil.Client.page("home");
 * String layout = PathUtil.Client.layoutMain();
 * </pre>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 17/11/2025
 * @since 1.0.0
 */
public final class PathUtil {

    private PathUtil() {
    }

    // ============================
    // Shared Area
    // ============================
    public static final class Shared {
        private static final String BASE = "/WEB-INF/views/shared";
        private static final String PAGES = BASE + "/pages";

        public static String page(String slug) {
            return PAGES + "/" + slug + ".jsp";
        }
    }

    // ============================
    // Client Area
    // ============================
    public static final class Client {
        private static final String BASE = "/WEB-INF/views/client";
        private static final String PAGES = BASE + "/pages";
        private static final String LAYOUTS = BASE + "/layouts";

        public static String page(String slug) {
            return PAGES + "/" + slug + ".jsp";
        }

        public static String layoutMain() {
            return LAYOUTS + "/main-layout.jsp";
        }
    }

    // ============================
    // Admin Area
    // ============================
    public static final class Admin {
        private static final String BASE = "/WEB-INF/views/admin";
        private static final String PAGES = BASE + "/pages";
        private static final String LAYOUTS = BASE + "/layouts";

        public static String page(String slug) {
            return PAGES + "/" + slug + ".jsp";
        }

        public static String layoutMain() {
            return LAYOUTS + "/admin-layout.jsp";
        }
    }
}