package com.laptrinhweb.zerostarcafe.utils;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

/**
 * Description: Servlet for performing health checks on the Zero Star Coffee application.
 * This servlet verifies that critical parts are properly configured and operational.
 *
 * <p>The health check performs the following validations:</p>
 * <ul>
 *   <li>JDBC driver availability - Verifies that MySQL JDBC driver is loaded</li>
 *   <li>Database connectivity - Tests the connection to the database using DataSource</li>
 * </ul>
 *
 * <p>Accessible at <code>/health</code> endpoint.</p>
 *
 * <p>Returns HTTP 200 if all checks pass, or HTTP 500 if any check fails.
 * The response includes detailed status messages for each component.</p>
 * <p>
 *
 * <h2> Example response: </h2>
 * <pre>
 * == Zero Star Coffee – Health Check ==
 * ✅ JDBC driver found.
 * ✅ Database connection OK.
 * </pre>
 *
 * @author Dang Van Trung
 * @date 21/10/2025
 */
@WebServlet("/health")
public class HealthCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain; charset=UTF-8");

        StringBuilder result = new StringBuilder();
        result.append("== Zero Star Coffee – Health Check ==\n");

        // 1️⃣ Check JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            result.append("✅ JDBC driver found.\n");
        } catch (ClassNotFoundException e) {
            result.append("❌ JDBC driver missing. Copy mysql-connector-j.jar to TOMCAT/lib.\n");
            res.setStatus(500);
        }

        // 2️⃣ Check DataSource and DB connection
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                result.append("✅ Database connection OK.\n");
            } else {
                result.append("❌ Database connection failed (null or closed).\n");
                res.setStatus(500);
            }
        } catch (Exception e) {
            result.append("❌ DataSource or database connection failed: ").append(e.getMessage()).append("\n");
            res.setStatus(500);
        }

        res.getWriter().print(result.toString());
    }
}
