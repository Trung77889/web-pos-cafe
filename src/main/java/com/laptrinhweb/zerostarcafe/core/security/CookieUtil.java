package com.laptrinhweb.zerostarcafe.core.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility for managing secure HTTP Cookies.
 * Provides helpers to create, get, and clear cookies
 * with secure defaults (HttpOnly, Secure, Path=/, SameSite=Strict).
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Create a secure cookie
 * Cookie cookie = CookieUtil.create("myCookie", "value", 3600);
 * response.addCookie(cookie);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public final class CookieUtil {

    private CookieUtil() {
    }

    /**
     * Creates a secure {@link Cookie} for authentication purposes.
     *
     * @param name          the cookie name
     * @param value         the cookie value
     * @param maxAgeSeconds the time to live (TTL) in seconds.
     *                      If <= 0, the cookie is a session cookie.
     * @return {@link Cookie} object
     */
    public static Cookie create(String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");

        if (maxAgeSeconds > 0) {
            cookie.setMaxAge(maxAgeSeconds);
        } else {
            // session cookie: exists until the browser is closed
            cookie.setMaxAge(-1);
        }

        return cookie;
    }

    /**
     * Gets the value of a cookie from the {@link HttpServletRequest}.
     *
     * @param request the current {@link HttpServletRequest}
     * @param name    the name of the cookie to get
     * @return the cookie value, or {@code null} if it does not exist
     */
    public static String get(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || name == null) {
            return null;
        }

        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    /**
     * Clears a cookie by overwriting it with empty value
     * and {@code maxAge = 0} (expires immediately)
     *
     * @param name the name of the cookie to clear
     * @param resp the {@link HttpServletResponse} to add the "clearing" cookie to
     */
    public static void clear(String name, HttpServletResponse resp) {
        if (name == null || resp == null) {
            return;
        }

        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);

        resp.addCookie(cookie);
    }
}