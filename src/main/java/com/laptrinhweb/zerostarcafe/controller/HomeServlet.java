package com.laptrinhweb.zerostarcafe.controller;

import com.laptrinhweb.zerostarcafe.utils.ViewPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/"})
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("pageTitle", "Trang chủ Zero Star Coffee");
        req.setAttribute("pageContent", ViewPath.Pages.HOME);
        req.getRequestDispatcher(ViewPath.Layout.MAIN).forward(req, resp);
    }
}