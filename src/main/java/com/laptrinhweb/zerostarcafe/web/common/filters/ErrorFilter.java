package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Catches unhandled exceptions from the filter chain and sends a 500 error
 * to the container's error handler.
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 17/11/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "ErrorFilter", urlPatterns = "/*")
public class ErrorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        try {
            chain.doFilter(req, resp);
        } catch (Exception e) {
            LoggerUtil.error(ErrorFilter.class, "Unhandled exception", e);

            if (response.isCommitted()) {
                LoggerUtil.warn(ErrorFilter.class, "Response already committed.");
                throw new ServletException(e);
            }

            request.setAttribute("jakarta.servlet.error.exception", e);
            request.setAttribute("jakarta.servlet.error.status_code", 500);
            response.sendError(500);
        }
    }
}
