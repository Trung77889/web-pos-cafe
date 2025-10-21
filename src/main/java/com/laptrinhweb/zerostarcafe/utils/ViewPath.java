package com.laptrinhweb.zerostarcafe.utils;

/**
 * Description: Centralized constants for JSP view paths.
 * Used to avoid hard-coded strings in controllers (Servlets),
 * improve IntelliJ navigation, and reduce maintenance cost
 * when moving or renaming JSP files.
 *
 * <h2> Example usage: </h2>
 * req.setAttribute("pageContent", ViewPath.Pages.HOME);
 * req.getRequestDispatcher(ViewPath.Layout.MAIN).forward(req, resp);
 *
 * @author Dang Van Trung
 * @date 21/10/2025
 */
public final class ViewPath {

    private ViewPath() {
        throw new AssertionError("ViewPath class should not be instantiated.");
    }

    // === Layouts ===
    public static final class Layout {
        private static final String BASE = "/WEB-INF/views/layout";
        public static final String MAIN = BASE + "/main-layout.jsp";
        public static final String HEADER = BASE + "/header.jsp";
        public static final String FOOTER = BASE + "/footer.jsp";
        public static final String HEAD = BASE + "/head.jsp";
        public static final String MODALS = BASE + "/modals.jsp";
    }

    // === Common Pages ===
    public static final class Pages {
        private static final String BASE = "/WEB-INF/views/pages";
        public static final String HOME = BASE + "/home.jsp";
        public static final String ERROR = BASE + "/error.jsp";
    }

    // === Admin Pages ===
    public static final class Admin {
        private static final String BASE = "/WEB-INF/views/admin";
        public static final String WELCOME = BASE + "/welcome.jsp";
        // Add more admin JSPs here as needed
    }

    // === User Pages ===
    public static final class User {
        private static final String BASE = "/WEB-INF/views/user";
        public static final String WELCOME = BASE + "/welcome.jsp";
        // Add user-related JSPs (profile, settings, etc.)
    }

    // === Other (optional future extension) ===
    public static final class Components {
        private static final String BASE = "/WEB-INF/views/components";
        // For reusable component JSPs (cards, toasts, partials, etc.)
    }
}