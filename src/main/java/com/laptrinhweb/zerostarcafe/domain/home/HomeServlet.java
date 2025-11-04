package com.laptrinhweb.zerostarcafe.domain.home;

import com.laptrinhweb.zerostarcafe.core.utils.ViewPathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("pageTitle", "general.page.home");
        req.setAttribute("pageContent", ViewPathUtil.Pages.HOME);
        req.getRequestDispatcher(ViewPathUtil.Layout.MAIN).forward(req, resp);
    }
}