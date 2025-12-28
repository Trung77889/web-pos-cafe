package com.laptrinhweb.zerostarcafe.web.common.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility class for extracting path parameters from servlet requests.
 * Supports RESTful URL patterns like /api/resource/{id} or /api/resource/{slug}.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // URL: /api/products/123
 * Long productId = PathParamUtil.extractLongParam(req, "Product ID");
 * 
 * // URL: /api/products/category/ca-phe-phin
 * String categorySlug = PathParamUtil.extractStringParam(req, "Category slug");
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
public final class PathParamUtil {
    private PathParamUtil() {}
    
    /**
     * Extracts a single path parameter and converts it to Long.
     * Use when URL pattern is /resource/{id}
     *
     * @param req       the HTTP request
     * @param paramName descriptive name for error messages
     * @return the extracted Long value
     * @throws IllegalArgumentException if parameter is missing or invalid
     */
    public static Long extractLongParam(HttpServletRequest req, String paramName) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        String param = pathInfo.replace("/", "").trim();
        if (param.isEmpty()) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        try {
            return Long.parseLong(param);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + paramName + ": " + param, e);
        }
    }
    
    /**
     * Extracts a single path parameter as String.
     * Use when parameter is not numeric (e.g., slug).
     *
     * @param req       the HTTP request
     * @param paramName descriptive name for error messages
     * @return the extracted String value
     * @throws IllegalArgumentException if parameter is missing
     */
    public static String extractStringParam(HttpServletRequest req, String paramName) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        String param = pathInfo.replace("/", "").trim();
        if (param.isEmpty()) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        return param;
    }
}
