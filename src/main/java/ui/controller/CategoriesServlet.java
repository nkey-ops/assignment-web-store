package ui.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.services.CategoriesService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.models.UserModel;

/**
 * CategoriesServlet
 */
@WebServlet(urlPatterns = "/categories")
public class CategoriesServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    private CategoriesService categoriesService;

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.categoriesService = (CategoriesService) 
                    servletContext.getAttribute("categoriesService");

        if (this.categoriesService== null)
            throw new RuntimeException(
                    "Dependency wasn't found in the context");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.debug("POST category request was sent");

         UserModel user = (UserModel) req.getSession().getAttribute("user");
        if (user.getType() != UserModel.Type.ADMIN) {
            resp.setStatus(403);
            resp.sendRedirect("forbidden.jsp");
        }

        String categoryName = req.getParameter("name");

        String params = "";
        if (categoryName == null || categoriesService.existsByName(categoryName)) {
            params = "errorCategory=Category already exists";
            resp.setStatus(400);
        } else {
            categoriesService.createCategory(categoryName);
            params = "successCategory=true";
        }

        resp.sendRedirect("/products-form?" + params);
    }
}
