package com.laptrinhweb.zerostarcafe.web.auth;

import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.Flash;
import com.laptrinhweb.zerostarcafe.core.utils.TimeUtil;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.LoginDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthService;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthSessionService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Handles user login: validate → authenticate → create session → issue cookies.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/auth/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService;
    private AuthSessionService authSessionService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();

        this.authService = new AuthService();
        this.authSessionService = (AuthSessionService)
                ctx.getAttribute(SecurityKeys.CTX_AUTH_SESSION_SERVICE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Flash flash = new Flash(req);
        String redirect = req.getHeader("Referer");

        // ==== Read parameter ====
        String username = req.getParameter("loginUsername");
        String password = req.getParameter("loginPassword");

        LoginDTO form = new LoginDTO(username, password);

        // ==== Validation input ====
        ValidationResult validation = form.validate();
        if (!validation.valid()) {
            flash.error("message.validation_failed")
                    .formResponse(form.formState(), validation.fieldErrors())
                    .set("openModal", "login")
                    .send();
            resp.sendRedirect(redirect);
            return;
        }

        // ==== Handle authentication ====
        AuthResult<AuthStatus, AuthUser> authResult = authService.login(form);
        if (!authResult.success()) {
            flash.error("message.login_failed")
                    .formResponse(form.formState(), Map.of())
                    .set("openModal", "login")
                    .send();
            resp.sendRedirect(redirect);
            return;
        }

        if (authSessionService == null) {
            flash.error("message.server_error").send();
            resp.sendRedirect(redirect);
            return;
        }

        // ===== Create session =====
        AuthUser authUser = authResult.data();
        HttpSession session = authSessionService.newLoginSession(req, authUser);
        if (session == null) {
            flash.error("message.server_error").send();
            resp.sendRedirect(redirect);
            return;
        }

        // ===== Read session info =====
        LocalDateTime expiredAt =
                (LocalDateTime) session.getAttribute(SecurityKeys.SESSION_EXPIRED_AT);
        String authToken =
                (String) session.getAttribute(SecurityKeys.SESSION_AUTH_TOKEN);
        String deviceId =
                (String) session.getAttribute(SecurityKeys.SESSION_DEVICE_ID);

        int ttlSeconds = TimeUtil.ttlFromNow(expiredAt);

        // ===== Attach cookies =====
        Cookie authTokenCookie = CookieUtil.create(
                SecurityKeys.COOKIE_AUTH_SESSION,
                authToken,
                ttlSeconds
        );
        Cookie deviceIdCookie = CookieUtil.create(
                SecurityKeys.COOKIE_DEVICE_ID,
                deviceId,
                ttlSeconds
        );

        resp.addCookie(authTokenCookie);
        resp.addCookie(deviceIdCookie);

        flash.success("message.login_success").send();
        resp.sendRedirect(redirect);
    }
}