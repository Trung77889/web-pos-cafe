package com.laptrinhweb.zerostarcafe.web.common;

import com.laptrinhweb.zerostarcafe.core.utils.PathUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Front servlet that routes friendly URLs to JSP pages and layouts.
 * This servlet is mapped to <b>"/"</b>, so the container still applies the
 * normal URL matching priority:
 * </p>
 *
 * <ol>
 *     <li>Exact mappings (e.g. {@code "/login"}, {@code "/error"}).</li>
 *     <li>Path mappings (e.g. {@code "/admin/*"}, {@code "/manager/*"}, {@code "/staff/*"}).</li>
 *     <li>Extension mappings (e.g. {@code "*.jsp"} handled by the JSP servlet).</li>
 *     <li>The default mapping {@code "/"} (this servlet) for everything else.</li>
 * </ol>
 *
 * <p>
 * If this servlet used {@code "/*"}, even internal JSP paths such as
 * {@code "/WEB-INF/views/client/layouts/main-layout.jsp"} or files included
 * via {@code <jsp:include>} would match the path mapping. <br/>
 * <p>
 * The controller would be called again for those JSPs, which can lead to
 * a routing loop. With {@code "/"}, these JSP resources (including {@code "/WEB-INF/*.jsp"})
 * are handled by the container’s JSP/default servlet and then inserted into
 * our layout, instead of being routed back through this front servlet.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * /home         -> /WEB-INF/views/client/pages/home.jsp
 * /admin/users  -> /WEB-INF/views/admin/pages/users.jsp
 * /staff/orders -> /WEB-INF/views/staff/pages/orders.jsp
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 17/11/2025
 * @since 1.0.0
 */
@WebServlet(name = "FrontController", urlPatterns = "/")
public class FrontController extends HttpServlet {

    /**
     * Represents the area (section) of the website.
     * CLIENT = public site.
     * ADMIN / MANAGER / STAFF = restricted areas.
     */
    private enum Area {CLIENT, ADMIN}

    /**
     * Detects which area the request belongs to based on the URL path.
     *
     * @param path full request path (e.g. "/admin/users")
     * @return the detected area
     */
    private Area detectArea(String path) {
        if (path.startsWith("/admin")) return Area.ADMIN;
        return Area.CLIENT;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String ctx = req.getContextPath();
        String path = req.getServletPath();

        // ==== Ignore static file path ====
        if (path.startsWith("/assets/")) {
            RequestDispatcher rd = getServletContext().getNamedDispatcher("default");
            rd.forward(req, resp);
            return;
        }

        // ==== Determine which area this request belongs to ====
        Area area = detectArea(path);
        req.setAttribute("area", area.name());

        // Extract last part of URL: "/admin/dashboard" -> "dashboard"
        String slug = path.substring(path.lastIndexOf("/") + 1);

        // Default redirect if path is empty
        if (slug.isEmpty()) {
            if (area == Area.CLIENT) {
                resp.sendRedirect(ctx + "/home");
                return;
            }
            if (area == Area.ADMIN) {
                resp.sendRedirect(ctx + "/admin/dashboard");
                return;
            }
        }

        String page;
        String layout;
        String titleKey;

        switch (area) {
            case ADMIN:
                page = PathUtil.Admin.page(slug);
                layout = PathUtil.Admin.layoutMain();
                titleKey = "general.admin." + slug;
                break;

            default:
                page = PathUtil.Client.page(slug);
                layout = PathUtil.Client.layoutMain();
                titleKey = "general.page." + slug;
                break;
        }

        // ==== Check if page exists ====
        if (getServletContext().getResource(page) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // ==== Forward to layout with resolved content ====
        req.setAttribute("pageTitle", titleKey);
        req.setAttribute("pageContent", page);
        req.getRequestDispatcher(layout).forward(req, resp);
    }
}