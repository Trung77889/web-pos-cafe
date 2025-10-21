package com.laptrinhweb.zerostarcafe.controller;

import com.laptrinhweb.zerostarcafe.dao.UserDAO;
import com.laptrinhweb.zerostarcafe.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.login(username, password);
        System.out.println("user: " + user.getFullName());

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            request.setAttribute("pageTitle", "Chào mừng đến với Zero Star Cafe");

            if (user.isSuperAdmin() || !user.getRoleCode().equals("customer")) {
                request.setAttribute("pageContent", "/WEB-INF/views/admin/welcome.jsp");
                request.getRequestDispatcher("/WEB-INF/views/layout/main-layout.jsp")
                        .forward(request, response);
            } else {
                request.setAttribute("pageContent", "/WEB-INF/views/user/welcome.jsp");
                request.getRequestDispatcher("/WEB-INF/views/layout/main-layout.jsp")
                        .forward(request, response);
            }
        } else {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.getRequestDispatcher("/WEB-INF/views/pages/error.jsp").forward(request, response);
        }
    }
}

