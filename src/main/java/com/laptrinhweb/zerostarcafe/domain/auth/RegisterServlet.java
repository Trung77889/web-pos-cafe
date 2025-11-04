package com.laptrinhweb.zerostarcafe.domain.auth;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.security.PasswordHashUtil;
import com.laptrinhweb.zerostarcafe.core.utils.AppLoggerUtil;
import com.laptrinhweb.zerostarcafe.core.utils.FlashUtil;
import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.user.User;
import com.laptrinhweb.zerostarcafe.domain.user.UserDAO;
import com.laptrinhweb.zerostarcafe.domain.user.UserValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Handles user registration requests using Server-Side Rendering (SSR).
 * Validates form input, checks for duplicate accounts, securely hashes
 * the password (Argon2), and persists the new user record. Validation
 * errors, form state, and modal visibility are preserved through session
 * attributes to provide a smooth UX when re-rendered via JSP.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * POST /auth/register
 * Body: email, username, password, agreeTerms
 * }
 * </pre>
 *
 * <h2>UI Behavior:</h2>
 * <pre>
 * {@code
 * - On validation failure → reopen "register" modal with errors
 * - On duplicate email/username → mark field as invalid
 * - On success → clear form state + push flash toast
 * }
 * </pre>
 *
 * <h2>Notes:</h2>
 * <p>
 * Implements Post-Redirect-Get (PRG) to prevent accidental form resubmission.
 * </p>
 *
 * @author Dang
 * @version 1.0.0
 * @lastModified 04/11/2025
 * @since 1.0.0
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ==== Read input ====
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String agreeTerms = req.getParameter("agreeTerms");

        // ==== Validation ====
        ValidationResult validation = UserValidator.validateRegistration(email, username, password, agreeTerms);
        if (!validation.valid()) {
            failAndReturn(req, resp, validation.fieldErrors(), email, username);
            return;
        }

        // ==== Database Logic ====
        try (Connection conn = DBConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(conn);

            // Duplicate check
            if (userDAO.existsByEmail(email)) {
                failAndReturn(req, resp,
                        Map.of("email", "form.email_exists"),
                        email, username
                );
                AppLoggerUtil.warn(getClass(), "Email already exists: " + email);
                return;
            }

            if (userDAO.existsByUsername(username)) {
                failAndReturn(req, resp,
                        Map.of("username", "form.username_exists"),
                        email, username
                );
                AppLoggerUtil.warn(getClass(), "Username already exists: " + username);
                return;
            }

            // Hash password securely (Argon2)
            String passwordHash = PasswordHashUtil.hash(password);

            // Insert a new user record
            User newUser = new User(email, username, passwordHash);
            boolean inserted = userDAO.insert(newUser);

            if (inserted) {
                clearFormState(req);
                req.getSession().setAttribute("flash", FlashUtil.success("message.register_success"));
                AppLoggerUtil.info(getClass(), "New user registered: " + newUser.getUsername());
            } else {
                req.getSession().setAttribute("flash", FlashUtil.error("message.register_failed"));
                AppLoggerUtil.warn(getClass(), "Insert failed: " + newUser.getUsername());
            }

            resp.sendRedirect(req.getHeader("Referer"));

        } catch (SQLException e) {
            req.getSession().setAttribute("flash", FlashUtil.error("message.server_error"));
            req.getSession().setAttribute("openModal", "register");
            resp.sendRedirect(req.getHeader("Referer"));
            AppLoggerUtil.error(getClass(), "Database exception", e);
        }
    }

    /**
     * Persists invalid form state into the session and redirects the user back
     * to the page. Used when validation or duplicate checks fail.
     *
     * @param req      current HTTP request
     * @param resp     current HTTP response
     * @param errors   map of field -> i18n error keys
     * @param email    original user email for refill
     * @param username original username for refill
     * @throws IOException redirect failure
     */
    private void failAndReturn(
            HttpServletRequest req,
            HttpServletResponse resp,
            Map<String, String> errors,
            String email,
            String username
    ) throws IOException {

        HttpSession s = req.getSession();

        s.setAttribute("formData", Map.of(
                "email", email,
                "username", username
        ));
        s.setAttribute("formErrors", errors);
        s.setAttribute("openModal", "register");

        resp.sendRedirect(req.getHeader("Referer"));
    }

    /**
     * Removes all form-related session attributes so that modal inputs do not
     * persist on a successful submit or page refresh.
     *
     * @param req current HTTP request
     */
    private void clearFormState(HttpServletRequest req) {
        HttpSession s = req.getSession();
        s.removeAttribute("formData");
        s.removeAttribute("formErrors");
        s.removeAttribute("openModal");
    }
}