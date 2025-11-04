package com.laptrinhweb.zerostarcafe.domain.home;

import com.laptrinhweb.zerostarcafe.core.utils.ViewPathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Handles fetch requests from the client to dynamically load modal JSP fragments
 * such as login and register forms. The servlet maps the requested modal name
 * (e.g., /modals/register) to the correct JSP path defined in
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/10/2025
 * @since 1.0.0
 */
@WebServlet(name = "ModalServlet", urlPatterns = "/modals/*")
public class ModalServlet extends HomeServlet {

    /**
     * Maps modal request paths to their corresponding JSP view paths.
     */
    private static final Map<String, String> MODAL_MAP = Map.of(
            "/register", ViewPathUtil.Modals.REGISTER,
            "/login", ViewPathUtil.Modals.LOGIN
    );

    /**
     * Handles GET requests for modal fragments.
     * Example: GET /modals/register → register-form.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/plain");
            resp.getWriter().write("Missing modal name");
            return;
        }

        String jspPath = MODAL_MAP.get(pathInfo);
        if (jspPath == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("text/plain");
            resp.getWriter().write("Modal not found");
            return;
        }

        req.getRequestDispatcher(jspPath).forward(req, resp);
    }
}
