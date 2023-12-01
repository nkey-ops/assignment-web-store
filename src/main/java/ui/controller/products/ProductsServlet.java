package ui.controller.products;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.services.ProductsService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.models.ProductModel;
import ui.models.ProductStatus;
import ui.models.UserModel;

/**
 * Servlet that manages actions related to items and shopping cart.
 */
@WebServlet(urlPatterns = {"/products", "/"})
public class ProductsServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();

    private ProductsService productsService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.productsService = (ProductsService) servletContext.getAttribute("productsService");

        if (this.productsService == null)
            throw new RuntimeException(
                    "Dependency wasn't found in the context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.debug("[GET] Get products page was requested");

        UserModel user = (UserModel) req.getSession().getAttribute("user");

        List<ProductModel> products = 
            user.getType() == UserModel.Type.ADMIN ?
                                     productsService.getAllProducts() : 
                                     productsService.getProductsNotChosenByUser(
                                                                user.getName());

        req.setAttribute("products", products);
        req.getRequestDispatcher("/products.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserModel user = (UserModel) req.getSession().getAttribute("user");
        if (user.getType() != UserModel.Type.ADMIN) {
            resp.setStatus(403);
            resp.sendRedirect("forbidden.jsp");

        }

        String method = req.getParameter("_method");

        if (method != null && method.equalsIgnoreCase("put")) {
            logger.debug("POST request was redirected to PUT");
            doPut(req, resp);
        } else {

            String name = req.getParameter("name");
            String price = req.getParameter("price");
            String category = req.getParameter("category");
            String quantity = req.getParameter("quantity");

            if (name == null || price == null
                    || category == null || quantity == null)
                resp.sendRedirect("/products/form?errorProduct=Ivalid Parameters");
            else {
                productsService.createProducts(name,
                        Double.parseDouble(price),
                        Integer.valueOf(quantity),
                        category);

                resp.sendRedirect("/products-form?successProduct=true");
            }

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel user = (UserModel) req.getSession().getAttribute("user");
        if (user.getType() != UserModel.Type.ADMIN) {
            resp.setStatus(403);
            resp.sendRedirect("forbidden.jsp");
        }

        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        String category = req.getParameter("category");
        System.out.println(req.getParameterMap());

        String params = "id=" + id;
        if (id == null || name == null
                || price == null || category == null)
            params += "&errorProduct=Ivalid Parameters";
        else {

            ProductModel p = new ProductModel(
                    Integer.valueOf(id),
                    name,
                    Double.parseDouble(price),
                    ProductStatus.ACTIVE,
                    category);

            productsService.updateProduct(p);

            params += "&successProduct=true";
        }

        resp.sendRedirect("/products-edit-form?" + params);
    }
}
