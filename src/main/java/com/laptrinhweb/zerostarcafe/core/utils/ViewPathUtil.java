package com.laptrinhweb.zerostarcafe.core.utils;

/**
 * <h2>Description:</h2>
 * <p>
 * Centralized constants for JSP view paths across the entire application.
 * This class helps avoid hard-coded strings inside controllers and services,
 * improving IntelliJ navigation and reducing maintenance effort when JSP files
 * are moved, renamed, or reorganized.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * // Inside a servlet:
 * req.setAttribute("pageContent", ViewPath.Pages.HOME);
 * req.getRequestDispatcher(ViewPath.Layout.MAIN).forward(req, resp);
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/10/2025
 * @since 1.0.0
 */
public final class ViewPathUtil {

    private ViewPathUtil() {
        throw new AssertionError("ViewPath class should not be instantiated.");
    }

    // === Layouts ===
    public static final class Layout {
        private static final String BASE = "/WEB-INF/views/layouts";
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
        public static final String MAIN = BASE + "/admin-layout.jsp";
        // Add more admin JSPs here as needed
    }

    // === User Pages ===
    public static final class User {
        private static final String BASE = "/WEB-INF/views/user";
        public static final String WELCOME = BASE + "/welcome.jsp";
        // Add user-related JSPs (profile, settings, etc.)
    }

    // === Reusable Components ===
    public static final class Components {
        private static final String BASE = "/WEB-INF/views/components";
        // For reusable component JSPs (cards, toasts, partials, etc.)
    }

    // === Modal Fragments ===
    public static final class Modals {
        private static final String BASE = "/WEB-INF/views/modals";
        public static final String REGISTER = BASE + "/register-form.jsp";
        public static final String LOGIN = BASE + "/login-form.jsp";
        // Add more modals here (e.g., FORGOT_PASSWORD, FEEDBACK)
    }
}