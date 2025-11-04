package com.laptrinhweb.zerostarcafe.web.filters;

import com.laptrinhweb.zerostarcafe.core.utils.AppLoggerUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Global error handling filter that catches unhandled exceptions thrown
 * during the filter chain execution. It logs the error, attaches standard
 * servlet error attributes, and delegates final rendering to the container's
 * error-page mechanism via HttpServletResponse#sendError.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Automatically triggered for any request under /* mapping
 * // No manual invocation required.
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 04/11/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "ErrorHandlerFilter", urlPatterns = {"/*"})
public class ErrorHandlerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            AppLoggerUtil.error(getClass(), "Unhandled exception in filter chain", e);

            if (response.isCommitted()) {
                AppLoggerUtil.warn(getClass(), "Response has already been committed. Unable to send error message.");
                throw new ServletException(e);
            }

            request.setAttribute("jakarta.servlet.error.exception", e);
            request.setAttribute("jakarta.servlet.error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
