package com.laptrinhweb.zerostarcafe.core.utils;

import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility helper for creating flash message maps used across Servlets.
 * Flash messages are short-lived notifications stored temporarily in the
 * session (Post-Redirect-Get) and displayed once in the next request,
 * typically via toast notifications or inline UI feedback.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // In a servlet:
 * req.getSession().setAttribute("flash", Flash.success("message.register_success"));
 *
 * // In JSP:
 * ${message[sessionScope.flash.msg]}
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/11/2025
 * @since 1.0.0
 */
public class FlashUtil {
    public static Map<String, String> success(String key) {
        return Map.of("type", "success", "msg", key);
    }

    public static Map<String, String> error(String key) {
        return Map.of("type", "error", "msg", key);
    }

    public static Map<String, String> info(String key) {
        return Map.of("type", "info", "msg", key);
    }

    public static Map<String, String> warn(String key) {
        return Map.of("type", "warn", "msg", key);
    }

    public static Map<String, String> normal(String key) {
        return Map.of("type", "normal", "msg", key);
    }
}
