package ui.controller.products;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.services.CategoriesService;
import bo.services.ProductsService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.models.ProductModel;
import ui.models.UserModel;

@WebServlet(urlPatterns = "/products-edit-form")
public class ProductEditFormServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    private CategoriesService categoriesService;
    private ProductsService productsService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();

        this.categoriesService = (CategoriesService) servletContext.getAttribute("categoriesService");
        this.productsService = (ProductsService) servletContext.getAttribute("productsService");

        if (this.categoriesService == null ||
                this.productsService == null)
            throw new RuntimeException(
                    "Dependency wasn't found in the context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("[GET] Get product edit form page was requested");
        UserModel user = (UserModel) req.getSession().getAttribute("user");

        if (user.getType() != UserModel.Type.ADMIN) {
            resp.setStatus(403);
            resp.sendRedirect("forbidden.jsp");

        }

        String id = req.getParameter("id");

        ProductModel product = productsService.getProduct(Integer.valueOf(id));
        List<String> categories = categoriesService.getAllCategories();

        req.setAttribute("product", product);
        req.setAttribute("categories", categories);
        req.setAttribute("users", List.of());
        
        req.getRequestDispatcher("/productEditForm.jsp").forward(req, resp);
    }
}
