
package ui.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.services.OrderService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ui.models.OrderModel;
import ui.models.UserModel;

/**
 * OrderPackagesServlet
 */
    
@WebServlet(urlPatterns = "/orders/package")
public class OrdersPackageServlet  extends HttpServlet{
    private static Logger logger = LogManager.getLogger();
    private OrderService orderService; 

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.orderService = (OrderService) 
                                 servletContext.getAttribute("orderService");

        if(this.orderService == null)
            throw new RuntimeException(
                        "Dependency wasn't found in the context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
                                        throws ServletException, IOException {
         logger.debug("[GET] All orders to package page was requested");

         UserModel user = (UserModel) req.getSession().getAttribute("user");
         if (user.getType() != UserModel.Type.WAREHOUSE_STAFF) {
             resp.setStatus(403);
             resp.sendRedirect("forbidden.jsp");

         }
        
         List<OrderModel> pendingOrders = orderService.getAllPendingOrders(); 

         req.setAttribute("orders", pendingOrders);
         req.getRequestDispatcher("/orders.jsp").forward(req, resp);
    }
}
