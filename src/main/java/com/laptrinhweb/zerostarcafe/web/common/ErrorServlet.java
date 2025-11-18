package com.laptrinhweb.zerostarcafe.web.common;

import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Renders the generic error page for any container-dispatched error.
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 17/11/2025
 * @since 1.0.0
 */
@WebServlet(name = "ErrorServlet", urlPatterns = "/error")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("pageTitle", "general.page.error");
        req.setAttribute("pageContent", PathUtil.Shared.page("error"));

        String area = (String) req.getAttribute("area");
        String layout = "ADMIN".equals(area)
                ? PathUtil.Admin.layoutMain()
                : PathUtil.Client.layoutMain();

        req.getRequestDispatcher(layout).forward(req, resp);
    }
}
