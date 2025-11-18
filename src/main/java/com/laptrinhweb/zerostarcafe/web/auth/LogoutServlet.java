package com.laptrinhweb.zerostarcafe.web.auth;

import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthSessionService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles user logout: revoke server session → clear cookies → invalidate HttpSession.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
@WebServlet(name = "LogoutServlet", urlPatterns = "/auth/logout")
public class LogoutServlet extends HttpServlet {

    private AuthSessionService authSessionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        this.authSessionService = (AuthSessionService)
                ctx.getAttribute(SecurityKeys.CTX_AUTH_SESSION_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String redirect = req.getContextPath();

        String authToken = CookieUtil.get(req, SecurityKeys.COOKIE_AUTH_SESSION);
        if (authSessionService != null && authToken != null) {
            authSessionService.logoutByToken(authToken);
        }

        HttpSession session = req.getSession(false);
        if (session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException ignored) {
                // Session already invalidated; ignore
            }
        }

        CookieUtil.clear(SecurityKeys.COOKIE_AUTH_SESSION, resp);
        CookieUtil.clear(SecurityKeys.COOKIE_DEVICE_ID, resp);

        resp.sendRedirect(redirect);
    }
}
