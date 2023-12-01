
package bo.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import db.entities.OrderEntity;
import db.entities.ProductEntity;
import db.repositories.OrderRepository;
import db.repositories.UsersRepository;
import ui.models.OrderModel;
import ui.models.OrderStatus;
import ui.models.ProductModel;
import ui.models.ProductStatus;
import ui.models.UserModel;

/**
 * OrderService
 */
public class OrderService {
    private final OrderRepository orderRepo;
    private final UsersRepository usersRepo;
    private final ProductsService productsService;
    private final Connection connection;

    public OrderService(OrderRepository orderRepo,
                        UsersRepository usersRepo,
                        ProductsService productsService,
                        Connection connection) {
        Objects.requireNonNull(orderRepo);
        Objects.requireNonNull(usersRepo);
        Objects.requireNonNull(productsService);
        Objects.requireNonNull(connection);

        this.orderRepo = orderRepo;
        this.usersRepo = usersRepo;
        this.productsService = productsService;
        this.connection = connection;
    } 

    //TODO Transaction 
    public void createOrder(String buyerName, List<ProductModel> products){
        Objects.requireNonNull(buyerName);
        Objects.requireNonNull(products);

        if(products.isEmpty()) 
               throw new IllegalArgumentException(
                       "Products list should not be empty");

        runTransaction(() -> {
          for (ProductModel product : products) {
            if(product.getStatus() != ProductStatus.ACTIVE ) 
              throw new IllegalArgumentException(
                  "Product status should be " + ProductStatus.ACTIVE ); 

            product.setStatus(ProductStatus.SOLD);  
            productsService.updateProduct(product);
          }
          
          List<ProductEntity> productEntities = new ArrayList<>();
          products.forEach(p ->
              productEntities.add(
                ProductsService.map(p)));   

          orderRepo.createOrder(buyerName, productEntities, false);
        }, "Couldn't create the order");
    }

    public List<OrderModel> getAllOrdersByUser(UserModel user) {
        Objects.requireNonNull(user);

        List<OrderEntity> orderEntities = 
                orderRepo.getAllOrdersByUsername(user.getName());

        List<OrderModel> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {

            List<ProductModel> orderedProducts = 
                productsService.getAllProductsByOrderId(orderEntity.getId());

            orders.add(map(orderEntity, orderedProducts));
        }

        return orders;
    }

    public List<OrderModel> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepo.getAllOrders();
        List<OrderModel> orders = new ArrayList<>();

        for (OrderEntity orderEntity : orderEntities) {

            List<ProductModel> orderedProducts = 
                productsService.getAllProductsByOrderId(orderEntity.getId());

            orders.add(map(orderEntity, orderedProducts));
        }

        return orders;
    }

    public List<OrderModel> getAllPendingOrders() {
        List<OrderEntity> orderEntities = 
            orderRepo.getAllOrdersByStatus(OrderStatus.PENDING);
        List<OrderModel> orders = new ArrayList<>();

        for (OrderEntity orderEntity : orderEntities) {
            List<ProductModel> orderedProducts = 
                productsService.getAllProductsByOrderId(orderEntity.getId());

            orders.add(map(orderEntity, orderedProducts));
        }

        return orders;
    }

    public void packageOrder(int id, String packagerName) {
        Objects.requireNonNull(packagerName);

        if(!orderRepo.existsById(id) || 
                !usersRepo.existsByUsername(packagerName))
            throw new IllegalArgumentException();

        orderRepo.packageOrder(id, packagerName);
    }

    static OrderModel map(OrderEntity orderEntity) {
        return map(orderEntity, List.of());
    }

    static OrderModel map(OrderEntity orderEntity, List<ProductModel> orderedProducts ) {
        Objects.requireNonNull(orderEntity);
        Objects.requireNonNull(orderedProducts);
        
        return new OrderModel(
                orderEntity.getId(),
                orderEntity.getBuyerName(),
                orderEntity.getPackagerName(),
                orderEntity.getStatus(),
                orderedProducts);

                
    }


    private void runTransaction(Runnable runnable, String errorMsg)  {
        try {
          connection.setAutoCommit(false);
          runnable.run();
          connection.commit();
        } catch (SQLException e) {
          try {
            connection.rollback(); 
          } catch (Exception e1) {
            e.addSuppressed(e1);
          }

            throw new RuntimeException(errorMsg, e);
        } finally {
          try {
            connection.setAutoCommit(true);
          } catch (Exception e) {
            throw new RuntimeException("Couldn't enable autocomit", e);
          }
        }
    }

}
