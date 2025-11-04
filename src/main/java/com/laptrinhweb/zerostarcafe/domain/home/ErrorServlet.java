package com.laptrinhweb.zerostarcafe.domain.home;

import com.laptrinhweb.zerostarcafe.core.utils.ViewPathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Centralized error view servlet responsible for rendering the standardized
 * error UI layout. Triggered automatically by the container when an error-page
 * mapping dispatches to "/error".
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Invoked indirectly via:
 * // response.sendError(500)
 * // or matched <error-page> configurations in web.xml
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 04/11/2025
 * @since 1.0.0
 */
@WebServlet(name = "ErrorServlet", urlPatterns = "/error")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "general.page.error");
        req.setAttribute("pageContent", ViewPathUtil.Pages.ERROR);
        req.getRequestDispatcher(ViewPathUtil.Layout.MAIN)
                .forward(req, resp);
    }
}
