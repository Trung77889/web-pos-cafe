package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthSessionService;
import com.laptrinhweb.zerostarcafe.domain.auth_token.AuthToken;
import com.laptrinhweb.zerostarcafe.domain.auth_token.AuthTokenDAO;
import com.laptrinhweb.zerostarcafe.domain.auth_token.AuthTokenDAOImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Global authentication filter that attaches the authenticated user
 * to the current request if a valid session or auth token is present.
 * It also performs periodic session ID rotation to reduce
 * session fixation risk.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 17/11/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

    private AuthSessionService authSessionService;

    @Override
    public void init(FilterConfig config) throws ServletException {
        Filter.super.init(config);
        ServletContext ctx = config.getServletContext();
        this.authSessionService = (AuthSessionService)
                ctx.getAttribute(SecurityKeys.CTX_AUTH_SESSION_SERVICE);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(false);

        // Cookies
        String cookieDeviceId = CookieUtil.get(request, SecurityKeys.COOKIE_DEVICE_ID);
        String cookieAuthToken = CookieUtil.get(request, SecurityKeys.COOKIE_AUTH_SESSION);

        // ===== 1. Already authenticated via HttpSession =====
        if (session != null) {
            AuthUser authUser = (AuthUser) session.getAttribute(SecurityKeys.SESSION_AUTH_USER);

            if (authUser != null) {

                // 1.1 Detect hijacking: deviceId must match the session’s original device
                String sessionDeviceId = (String) session.getAttribute(SecurityKeys.SESSION_DEVICE_ID);
                if (cookieDeviceId == null || !cookieDeviceId.equals(sessionDeviceId)) {

                    // Invalidate everything immediately (session + token + cookies)
                    authSessionService.logoutByToken(
                            (String) session.getAttribute(SecurityKeys.SESSION_AUTH_TOKEN)
                    );
                    session.invalidate();

                    CookieUtil.clear(SecurityKeys.COOKIE_AUTH_SESSION, response);
                    CookieUtil.clear(SecurityKeys.COOKIE_DEVICE_ID, response);

                    response.sendRedirect("/login");
                    return;
                }

                // 1.2 Mitigate session fixation by rotating sessionId periodically
                rotateSessionIfNeeded(request, session);

                chain.doFilter(req, resp);
                return;
            }
        }

        // ===== 2. No HttpSession → Try restore from auth_token =====
        if (cookieAuthToken != null && authSessionService != null) {

            String authHash = TokenUtil.hashToken(cookieAuthToken);

            // 2.1 Lookup auth token in DB (must be active & not expired)
            AuthToken tokenFromDB;
            try (Connection conn = DBConnection.getConnection()) {
                AuthTokenDAO dao = new AuthTokenDAOImpl(conn);
                tokenFromDB = dao.findValidByAuthHash(authHash).orElse(null);
            } catch (SQLException e) {
                failAndClear(cookieAuthToken, response, authSessionService);
                chain.doFilter(req, resp);
                return;
            }

            // Token missing or expired
            if (tokenFromDB == null) {
                failAndClear(cookieAuthToken, response, authSessionService);
                chain.doFilter(req, resp);
                return;
            }

            // 2.2 DeviceID mismatch → token was stolen or used on new device
            if (!tokenFromDB.getDeviceId().equals(cookieDeviceId)) {
                failAndClear(cookieAuthToken, response, authSessionService);
                chain.doFilter(req, resp);
                return;
            }

            // 2.3 Token + Device are valid → create fresh HttpSession
            HttpSession newSession = authSessionService.restoreByToken(request, cookieAuthToken);

            if (newSession != null) {
                chain.doFilter(req, resp);
                return;
            }

            // Fallback: token was valid but restoration failed
            failAndClear(cookieAuthToken, response, authSessionService);
        }

        // ===== 3. Anonymous request =====
        chain.doFilter(req, resp);
    }

    private void rotateSessionIfNeeded(HttpServletRequest request, HttpSession session) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastRotated = (LocalDateTime)
                session.getAttribute(SecurityKeys.SESSION_LAST_ROTATED);

        if (lastRotated == null ||
                Duration.between(lastRotated, now).toMinutes() >= 15) {
            request.changeSessionId();
            session.setAttribute(SecurityKeys.SESSION_LAST_ROTATED, now);
        }
    }

    private void failAndClear(String token, HttpServletResponse resp, AuthSessionService service) {
        service.logoutByToken(token);
        CookieUtil.clear(SecurityKeys.COOKIE_AUTH_SESSION, resp);
        CookieUtil.clear(SecurityKeys.COOKIE_DEVICE_ID, resp);
    }
}