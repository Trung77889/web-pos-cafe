package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Logs each dynamic HTTP request with method, URI, status, and response time.
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "LogFilter", urlPatterns = "/*")
public class LogFilter implements Filter {

    private static final String STATIC_REGEX =
            ".*\\.(css|js|png|jpg|jpeg|gif|svg|map|scss|webp|ico|woff|woff2|ttf)$";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        long start = System.currentTimeMillis();

        // Ignore static files for cleaner logs
        if (uri.matches(STATIC_REGEX)) {
            chain.doFilter(req, res);
            return;
        }

        chain.doFilter(req, res);

        long duration = System.currentTimeMillis() - start;

        LoggerUtil.info(
                LogFilter.class,
                String.format("%-4s %-40s | %3d | %4d ms",
                        request.getMethod(),
                        uri,
                        response.getStatus(),
                        duration)
        );
    }
}