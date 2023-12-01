import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bo.ServiceManager;
import bo.services.CategoriesService;
import bo.services.OrderService;
import bo.services.ProductsService;
import bo.services.UsersService;
import db.DBManager;
import db.entities.UserEntity;
import db.repositories.CategoriesRepository;
import db.repositories.ShoppingCartRepository;
import db.repositories.UsersRepository;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ui.models.ProductModel;
import ui.models.UserModel;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static Logger log = LogManager.getLogger();
    private DBManager dbManager;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();

        DBManager dbManager = DBManager.getInstance();
        ServiceManager serviceManager = ServiceManager.getInstance();

        ctx.setAttribute("usersService", serviceManager.getUsersService());
        log.debug("{} was added", UsersService.class.getSimpleName());

        ctx.setAttribute("orderService", serviceManager.getOrderService());
        log.debug("{} was added", OrderService.class.getSimpleName());

        ctx.setAttribute("productsService", serviceManager.getProductsService());
        log.debug("{} was added", ProductsService.class.getSimpleName());

        ctx.setAttribute("shoppingCartService", serviceManager.getShoppingCartService());
        log.debug("{} was added", ShoppingCartRepository.class.getSimpleName());

        ctx.setAttribute("categoriesService", serviceManager.getCategoriesService());
        log.debug("{} was added", CategoriesService.class.getSimpleName());

        log.info("Context was intialized");

        populateWithMockData(
                dbManager.getUsersRepo(),
                dbManager.getCategoriesRepo(),
                dbManager.getShoppingCartRepo(),
                serviceManager.getProductsService(),
                serviceManager.getOrderService());

        log.info("Database was populated with mock data");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        dbManager.close();
        log.info("Context was destroyed");
    }

    public void populateWithMockData(
            UsersRepository usersRepo,
            CategoriesRepository categoriesRepository, 
            ShoppingCartRepository shoppingCartRepo,
            ProductsService productsService, 
            OrderService orderService) {

        if (usersRepo.existsByUsername("client"))
            return;

        UserEntity user  = usersRepo.register("client", "pass", UserModel.Type.CLIENT);
        UserEntity staff = usersRepo.register("staff", "pass", UserModel.Type.WAREHOUSE_STAFF);
        UserEntity admin = usersRepo.register("admin", "pass", UserModel.Type.ADMIN);

        categoriesRepository.createCategory("Phones");
        categoriesRepository.createCategory("Food");
        categoriesRepository.createCategory("Drinks");

        List<ProductModel> p1 = productsService.createProducts("Pepsi", 20, 14, "Drinks");
        List<ProductModel> p2 = productsService.createProducts("Cola", 24, 12, "Drinks");
        List<ProductModel> p3 = productsService.createProducts("Phone", 1500, 10, "Phones");
        List<ProductModel> p4 = productsService.createProducts("IPad", 2000, 5, "Phones");


        for (int i = 0; i < 3; i++) {
            shoppingCartRepo.addProduct(user.getName(), p1.get(i).getId());
            shoppingCartRepo.addProduct(user.getName(), p2.get(i).getId());
            shoppingCartRepo.addProduct(user.getName(), p3.get(i).getId());
            shoppingCartRepo.addProduct(user.getName(), p4.get(i).getId());
        }

        List<ProductModel> orderedProducts = 
            List.of(p1.get(0), p1.get(1),
                    p2.get(0), p3.get(0),
                    p4.get(0));


        orderService.createOrder(user.getName(), orderedProducts);
        orderService.createOrder(user.getName(), List.of(p2.get(2)));
    }

}
