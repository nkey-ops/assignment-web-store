package bo;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.services.CategoriesService;
import bo.services.OrderService;
import bo.services.ProductsService;
import bo.services.ShoppingCartService;
import bo.services.UsersService;
import db.DBManager;
import lombok.Getter;

/**
 * ServiceManager
 */
@Getter
public class ServiceManager {
    private static Logger logger = LogManager.getLogger();
    
    private static ServiceManager serviceManager;
    private final UsersService usersService;
    private final OrderService orderService;
    private final ProductsService productsService; 
    private final ShoppingCartService shoppingCartService; 
    private final CategoriesService categoriesService; 
        
    private ServiceManager() {
        DBManager dbManager = DBManager.getInstance();
        this.usersService = new UsersService(dbManager.getUsersRepo());
        this.productsService = new ProductsService(
                                    dbManager.getProductsRepo(),
                                    usersService);
        this.orderService = new OrderService(
                                    dbManager.getOrderRepo(),
                                    dbManager.getUsersRepo(),
                                    productsService,
                                    dbManager.getConnection()); 
        this.shoppingCartService = 
                        new ShoppingCartService(
                                 dbManager.getShoppingCartRepo(),
                                 dbManager.getUsersRepo(),
                                 dbManager.getProductsRepo());

        this.categoriesService = new CategoriesService(
                                dbManager.getCategoriesRepo());

        logger.debug("Service Manager was initialized");
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public static ServiceManager getInstance() {
        if(Objects.isNull(serviceManager))
            serviceManager = new ServiceManager();
        return serviceManager;
    }
}
    

