package ui.controller.products;

import java.io.IOException;
import java.util.List;

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
 * ProductFormServelet
 */
@WebServlet(urlPatterns = "/products-form")
public class ProductFormServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    private CategoriesService categoriesService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        this.categoriesService = (CategoriesService) 
                        servletContext.getAttribute("categoriesService");

        if (this.categoriesService == null)
            throw new RuntimeException(
                    "Dependency wasn't found in the context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("[GET] Get products page was requested");
        UserModel user = (UserModel) req.getSession().getAttribute("user");

        if (user.getType() != UserModel.Type.ADMIN) {
            resp.setStatus(403);
            resp.sendRedirect("forbidden.jsp");

        }

        List<String> categories = categoriesService.getAllCategories();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/productForm.jsp").forward(req, resp);
    }
}
