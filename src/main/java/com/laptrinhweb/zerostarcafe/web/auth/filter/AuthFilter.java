package com.laptrinhweb.zerostarcafe.web.auth.filter;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.ContextUtil;
import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.request.AuthReqInfo;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthReqService;
import com.laptrinhweb.zerostarcafe.web.auth.mapper.AuthReqInfoMapper;
import com.laptrinhweb.zerostarcafe.web.auth.session.AuthSessionManager;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Global authentication filter. It checks user sessions,
 * validates auth cookies, restores missing sessions,
 * and supports token/session rotation.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.1.0
 * @lastModified 29/11/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

    private AuthSessionManager sessionManager;

    @Override
    public void init(FilterConfig config) throws ServletException {
        Filter.super.init(config);
        ServletContext ctx = config.getServletContext();

        // Load shared AuthSessionManager from servlet context
        this.sessionManager = ContextUtil.require(
                ctx, SecurityKeys.CTX_AUTH_SESSION_MANAGER, AuthSessionManager.class);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // Skip authentication for static files or when manager is missing
        String path = request.getServletPath();
        if (PathUtil.isStatic(path) || sessionManager == null) {
            chain.doFilter(req, resp);
            return;
        }

        // Prepare authentication data
        AuthReqInfo reqInfo = AuthReqInfoMapper.from(request);
        AuthContext currentContext = sessionManager.getContext(request);

        // Run request handling logic (validate / restore / rotate)
        AuthReqService authReqService = new AuthReqService();
        AuthResult<AuthStatus, AuthContext> result =
                authReqService.handleRequest(reqInfo, currentContext);

        // Perform action based on authentication status
        switch (result.status()) {
            case SESSION_ROTATED: {
                // Token and session ID rotated → update session & cookies
                sessionManager.refreshSession(request, response, result.data());
                break;
            }
            case SESSION_RESTORED: {
                // No valid session → restore a new one from cookies
                sessionManager.startSession(request, response, result.data());
                break;
            }
            case SESSION_INVALID: {
                // Invalid cookies or expired tokens → force logout
                sessionManager.endSession(request, response);
                break;
            }
            case SESSION_REUSED, SESSION_NOT_FOUND:
            default: {
                // Nothing to update → continue request
            }
        }

        chain.doFilter(req, resp);
    }
}