package com.laptrinhweb.zerostarcafe.web.auth.servlet;

import com.laptrinhweb.zerostarcafe.core.utils.Flash;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RegisterDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user register: validate → register.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Flash flash = new Flash(req);
        String redirect = req.getHeader("Referer");

        // ==== Read parameter ====
        String email = req.getParameter("regEmail");
        String username = req.getParameter("regUsername");
        String password = req.getParameter("regPassword");
        String agreeTerms = req.getParameter("agreeTerms");

        RegisterDTO form = new RegisterDTO(email, username, password, agreeTerms);

        // ==== Validation input ====
        ValidationResult validation = form.validate();
        if (!validation.valid()) {
            flash.error("message.validation_failed")
                    .formResponse(form.formState(), validation.fieldErrors())
                    .set("openModal", "register")
                    .send();
            resp.sendRedirect(redirect);
            return;
        }

        // ==== Handle registration ====
        AuthResult<AuthStatus, Void> authResult = authService.register(form);

        if (authResult.success()) {
            flash.success("message.register_success").send();
            resp.sendRedirect(redirect);
            return;
        }

        Map<String, String> fieldErrors = new HashMap<>();
        if (authResult.status() == AuthStatus.EMAIL_EXISTS)
            fieldErrors.put("regEmail", "form.email_exists");
        if (authResult.status() == AuthStatus.USERNAME_EXISTS)
            fieldErrors.put("regUsername", "form.username_exists");

        flash.error("message.register_failed")
                .formResponse(form.formState(), fieldErrors)
                .set("openModal", "register")
                .send();

        resp.sendRedirect(redirect);
    }
}