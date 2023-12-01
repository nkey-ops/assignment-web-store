package ui.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * LogoutServlet
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Logging out");
        req.getSession().invalidate();
        
        resp.sendRedirect("welcome.jsp");
    }
}

