package com.laptrinhweb.zerostarcafe.controller;

import com.laptrinhweb.zerostarcafe.dao.UserDAO;
import com.laptrinhweb.zerostarcafe.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = request.getParameter("fullname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User(fullName, username, email, password);
        boolean ok = userDAO.register(user);

        if (ok) {
            response.sendRedirect("views/user/welcome.jsp?success=1");
        } else {
            request.setAttribute("error", "Đăng ký thất bại, thử lại!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}

