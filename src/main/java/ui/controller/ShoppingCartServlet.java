package ui.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.services.ProductsService;
import bo.services.ShoppingCartService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.models.ProductModel;
import ui.models.UserModel;

/**
 * ShoppingCartServlet
 */
@WebServlet(urlPatterns = "/cart")
public class ShoppingCartServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();
    private ShoppingCartService shoppingCartService; 
    private ProductsService productsService; 

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.shoppingCartService = (ShoppingCartService) 
                            servletContext.getAttribute("shoppingCartService");
        this.productsService = (ProductsService) 
                            servletContext.getAttribute("productsService");

        if (this.productsService == null ||
            this.shoppingCartService == null)
            throw new RuntimeException(
                    "Dependency wasn't found in the context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel user = (UserModel) req.getSession().getAttribute("user");

        List<ProductModel> products = productsService.getProductsByUser(user.getName());
        products.sort((a, b) -> a.getName().compareTo(b.getName()));

        req.setAttribute("products", products);

        req.getRequestDispatcher("/shoppingBasket.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String method = (String) req.getParameter("method");

        if (method != null && method.equalsIgnoreCase("DELETE")) {
            logger.debug("POST request was redirected to DELETE");
            doDelete(req, resp);
        } else {
            logger.debug("POST request was sent");

            UserModel user = (UserModel) req.getSession().getAttribute("user");
            int id = Integer.valueOf(req.getParameter("id"));

            shoppingCartService.addProduct(user.getName(), id);

            resp.sendRedirect("/products");
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
                                    throws ServletException, IOException {

        logger.debug("DELETE request was sent");
        UserModel user = (UserModel) req.getSession().getAttribute("user");

        if (req.getParameter("id") != null) {
            int pid = Integer.valueOf(req.getParameter("id"));
            shoppingCartService.removeProduct(user.getName(), pid);
        } else {
            shoppingCartService.removeAll(user.getName());
        }

        resp.sendRedirect("/cart");
    }

}
