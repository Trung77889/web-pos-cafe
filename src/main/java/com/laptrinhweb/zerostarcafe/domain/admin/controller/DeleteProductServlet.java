package com.laptrinhweb.zerostarcafe.domain.admin.controller;

import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DeleteProductServlet", value = "/admin/api/delete-product")
public class DeleteProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("product-id"));
            AdminDAO dao = new AdminDAO();
            boolean isDeleted = dao.deleteProduct(id, 1);
            if (isDeleted) {
                request.getSession().setAttribute("message", "Product deleted successfully");
                request.getSession().setAttribute("messageType", "success");
            } else {
                request.getSession().setAttribute("message", "Error");
                request.getSession().setAttribute("messageType", "error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "System error");
            request.getSession().setAttribute("messageType", "error");
        }
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}