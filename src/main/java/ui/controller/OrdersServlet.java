package ui.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.services.OrderService;
import bo.services.ProductsService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.models.OrderModel;
import ui.models.ProductModel;
import ui.models.UserModel;

/**
 * OrdersServlet
 */
@WebServlet(urlPatterns = "/orders")
public class OrdersServlet extends HttpServlet{
    private static Logger logger = LogManager.getLogger();
    private OrderService orderService; 
    private ProductsService productsService; 

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.orderService = (OrderService) 
                                 servletContext.getAttribute("orderService");
        this.productsService = (ProductsService) 
                                 servletContext.getAttribute("productsService");

        if(this.orderService == null)
            throw new RuntimeException(
                        "Dependency wasn't found in the context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
                                        throws ServletException, IOException {
         logger.debug("[GET] Get all orders page was requested");

         UserModel user = (UserModel) req.getSession().getAttribute("user");

         List<OrderModel> orders = 
                 user.getType() == UserModel.Type.ADMIN
                 || user.getType() == UserModel.Type.WAREHOUSE_STAFF
                         ? orderService.getAllOrders()
                         : orderService.getAllOrdersByUser(user);

         req.setAttribute("orders", orders);
         req.getRequestDispatcher("/orders.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                                         throws ServletException, IOException {

         UserModel user = (UserModel) req.getSession().getAttribute("user");
        
         String method = req.getParameter("_method");

         if(method != null && method.equalsIgnoreCase("PUT")) {
            logger.debug("[POST] request was reddirect to PUT");
            doPut(req, resp);
         }else {
             logger.debug("[POST] Order all the products was requested");

             List<ProductModel> cartProducts = 
                 productsService.getProductsByUser(user.getName());

             orderService.createOrder(user.getName(), cartProducts);

             resp.sendRedirect("/orders");
         }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
                                         throws ServletException, IOException {
         logger.debug("[PUT] Order packaging was requested");

         UserModel user = (UserModel) req.getSession().getAttribute("user");
         if (user.getType() != UserModel.Type.WAREHOUSE_STAFF) {
             resp.setStatus(403);
             resp.sendRedirect("forbidden.jsp");
         }

         String id = req.getParameter("id"); 

         orderService.packageOrder(Integer.valueOf(id), user.getName());

         resp.sendRedirect("/orders");
    }
    

}
