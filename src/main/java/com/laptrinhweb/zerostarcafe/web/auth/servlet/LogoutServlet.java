package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.ContextUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthService;
import com.laptrinhweb.zerostarcafe.web.auth.session.AuthSessionManager;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles user logout: revoke server session → clear cookies → invalidate HttpSession.
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 29/11/2025
 * @since 1.0.0
 */
@WebServlet(name = "LogoutServlet", urlPatterns = "/auth/logout")
public class LogoutServlet extends HttpServlet {

    private AuthSessionManager sessionManager;
    private final AuthService authService = new AuthService();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();

        this.sessionManager = ContextUtil.require(
                ctx, SecurityKeys.CTX_AUTH_SESSION_MANAGER, AuthSessionManager.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AuthContext context = sessionManager.getContext(req);

        if (context != null && context.isValid()) {
            String authToken = context.getTokenValue(SecurityKeys.TOKEN_AUTH);
            if (authToken != null && !authToken.isBlank()) {
                authService.clearAuthState(authToken);
            }
        }

        sessionManager.endSession(req, resp);
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}