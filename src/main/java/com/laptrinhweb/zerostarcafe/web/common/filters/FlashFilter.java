package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.utils.Flash;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

/**
 * Moves flash data from session to request, then clears it (PRG support).
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 09/11/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "FlashScopeFilter", urlPatterns = "/*")
public class FlashFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession(false);

        if (session != null) {
            synchronized (session) {
                Object bag = session.getAttribute(Flash.FLASH_KEY);

                if (bag instanceof Map<?, ?> flash && !flash.isEmpty()) {
                    flash.forEach((k, v) ->
                            request.setAttribute(String.valueOf(k), v)
                    );
                    session.removeAttribute(Flash.FLASH_KEY);
                }
            }
        }

        chain.doFilter(req, resp);
    }
}