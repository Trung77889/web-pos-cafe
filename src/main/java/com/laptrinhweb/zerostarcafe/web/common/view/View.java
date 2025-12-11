package com.laptrinhweb.zerostarcafe.web.common.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a resolved view, including its layout, page path, title key,
 * and UI area (client/admin). Instances are typically created through
 * {@link ViewResolver} using semantic URLs rather than direct JSP paths.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * View view = View.client("/home");
 * View.render(view, req, resp);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/12/2025
 * @since 1.0.0
 */
public record View(
        ViewArea area,
        String titleKey,
        String pagePath,
        String layoutPath
) {

    public static final String AREA_KEY = "area";
    public static final String PAGE_TITLE = "pageTitle";
    public static final String PAGE_CONTENT = "pageContent";

    public static View client(String viewPath) {
        return ViewResolver.resolve(ViewArea.CLIENT, viewPath);
    }

    public static View admin(String viewPath) {
        return ViewResolver.resolve(ViewArea.ADMIN, viewPath);
    }

    /**
     * Renders the given view by forwarding the request to its layout JSP. </br>
     * Sends a 404 response if the view is {@code null} or the page does not exist.
     *
     * @param view the resolved view to render
     * @param req  current HTTP request
     * @param resp current HTTP response
     * @throws ServletException if the forwarding fails
     * @throws IOException      if the response cannot be written
     */
    public static void render(View view, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (view == null || req.getServletContext().getResource(view.pagePath()) == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute(AREA_KEY, view.area);
        req.setAttribute(PAGE_TITLE, view.titleKey);
        req.setAttribute(PAGE_CONTENT, view.pagePath);

        req.getRequestDispatcher(view.layoutPath()).forward(req, resp);
    }
}