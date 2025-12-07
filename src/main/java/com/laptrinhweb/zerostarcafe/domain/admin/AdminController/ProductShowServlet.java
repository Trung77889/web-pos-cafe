package com.laptrinhweb.zerostarcafe.domain.admin.AdminController;

import com.laptrinhweb.zerostarcafe.domain.admin.AdminDAO.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ProductShowServlet", value = "/admin/products")
public class ProductShowServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        try {
            List<Product> productsList = dao.getAllProductsByStore(1);

            request.setAttribute("productsList", productsList);
            request.setAttribute("pageContent", "/WEB-INF/views/admin/pages/dashboard.jsp");
            request.getRequestDispatcher("/WEB-INF/views/admin/layouts/admin-layout.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}