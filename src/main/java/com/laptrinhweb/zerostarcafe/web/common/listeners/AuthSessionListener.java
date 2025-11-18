package com.laptrinhweb.zerostarcafe.web.common.listeners;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthSessionManager;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthSessionService;
import com.laptrinhweb.zerostarcafe.domain.auth_token.AuthTokenService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Initializes the shared AuthSessionService at application startup.
 *
 * @author Dang Van Trung
 * @version 1.1.0
 * @lastModified 18/11/2025
 * @since 1.0.0
 */
@WebListener
public class AuthSessionListener implements ServletContextListener {

    private AuthSessionManager sessionManager;
    private AuthTokenService tokenService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();

        // Enforce single active session per user
        var userSessions = new ConcurrentHashMap<Long, HttpSession>();

        sessionManager = new AuthSessionManager(userSessions);
        tokenService = new AuthTokenService();
        AuthSessionService sessionService = new AuthSessionService(sessionManager, tokenService);

        ctx.setAttribute(SecurityKeys.CTX_AUTH_SESSION_SERVICE, sessionService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

        // When the server shuts down, force all users to log out
        for (var entry : sessionManager.getUserSessions().entrySet()) {
            Long userId = entry.getKey();
            HttpSession session = entry.getValue();

            // 1. Revoke all token by userId
            tokenService.revokeAllByUserId(userId);

            // 2. Invalidate all active session
            try {
                session.invalidate();
            } catch (IllegalStateException ignore) {
                // Already invalidated
            }
        }

        sessionManager.getUserSessions().clear();
        event.getServletContext().removeAttribute(SecurityKeys.CTX_AUTH_SESSION_SERVICE);
    }
}