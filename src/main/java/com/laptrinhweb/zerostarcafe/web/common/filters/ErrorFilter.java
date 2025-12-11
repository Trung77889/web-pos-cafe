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
 * @version 1.0.2
 * @lastModified 11/12/2025
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
            if (response.isCommitted()) {
                LoggerUtil.warn(getClass(), "Response already committed.");
                throw new ServletException(e);
            }

            LoggerUtil.error(getClass(), "Unhandled exception", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}