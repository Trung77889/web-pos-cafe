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
 * Global filter that logs each HTTP request and its response time.
 * Static files (CSS, JS, images, etc.) are ignored to reduce noise.
 * </p>
 *
 * <h2>Example:</h2>
 * <pre>
 * GET  /zero_star_cafe/modals/register  | 200 |  53 ms
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/10/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "LoggingFilter", urlPatterns = {"/*"})
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String method = request.getMethod();
        String uri = request.getRequestURI();
        long start = System.currentTimeMillis();

        if (uri.matches(".*\\.(css|js|png|jpg|jpeg|gif|svg|map|scss)$")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
        long duration = System.currentTimeMillis() - start;

        String logLine = String.format(
                "%-4s %-40s | %3d | %4d ms",
                method,
                uri,
                response.getStatus(),
                duration
        );

        AppLoggerUtil.info(getClass(), logLine);
    }
}