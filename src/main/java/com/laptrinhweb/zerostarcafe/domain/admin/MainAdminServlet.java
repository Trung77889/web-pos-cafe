package com.laptrinhweb.zerostarcafe.domain.admin;

import com.laptrinhweb.zerostarcafe.core.utils.ViewPathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/home")
public class MainAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageContent", ViewPathUtil.Admin.WELCOME);
        request.getRequestDispatcher(ViewPathUtil.Admin.MAIN).forward(request, response);
    }
}