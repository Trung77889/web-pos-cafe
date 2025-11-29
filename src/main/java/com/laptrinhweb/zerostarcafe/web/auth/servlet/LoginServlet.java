package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.ContextUtil;
import com.laptrinhweb.zerostarcafe.core.utils.Flash;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.LoginDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.request.AuthReqInfo;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthService;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserRole;
import com.laptrinhweb.zerostarcafe.web.auth.session.AuthSessionManager;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Handles user login: validate → authenticate → create session → issue cookies.
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 29/11/2025
 * @since 1.0.0
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/auth/login")
public class LoginServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Flash flash = new Flash(req);
        String redirect = req.getHeader("Referer");

        String username = req.getParameter("loginUsername");
        String password = req.getParameter("loginPassword");

        LoginDTO form = new LoginDTO(username, password);
        ValidationResult validation = form.validate();

        if (!validation.valid()) {
            flash.error("message.validation_failed").send();
            resp.sendRedirect(redirect);
            return;
        }

        AuthReqInfo reqInfo = new AuthReqInfo(
                req.getRemoteAddr(),
                req.getHeader("User-Agent"),
                CookieUtil.getAll(req)
        );

        AuthResult<AuthStatus, AuthContext> result = authService.authenticate(form, reqInfo);
        AuthContext context = result.data();

        if (context == null || !result.success()) {
            flash.error("message.login_failed")
                    .formResponse(form.formState(), Map.of())
                    .set("openModal", "login")
                    .send();
            resp.sendRedirect(redirect);
            return;
        }

        sessionManager.startSession(req, resp, context);
        flash.success("message.login_success").send();
        String target = getRedirectPath(context, req.getContextPath(), redirect);
        resp.sendRedirect(target);
    }

    /**
     * Determines redirect path based on user role.
     */
    private String getRedirectPath(AuthContext ctx, String ctxPath, String fallback) {
        if (ctx == null || ctx.getAuthUser() == null)
            return fallback;

        var user = ctx.getAuthUser();

        if (user.hasRole(UserRole.SUPER_ADMIN))
            return ctxPath + "/admin/dashboard";

        if (user.hasRole(UserRole.STORE_MANAGER))
            return ctxPath + "/store-manager/dashboard";

        if (user.hasRole(UserRole.STAFF))
            return ctxPath + "/staff/home";

        // Normal user → redirect to original page
        return fallback;
    }
}