package db.repositories;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.entities.OrderEntity;
import db.entities.ProductEntity;
import ui.models.OrderStatus;

/**
 * OrderRepository
 */
public class OrderRepository {
    private static Logger logger = LogManager.getLogger();
    private final Connection con;

    public OrderRepository(Connection con) {
        this.con = con;

        createOrderTable();
        createOrdersAndProductsOneToManyTable();
    }

    private void createOrderTable() {
        try (Statement s = con.createStatement()) {
            s.execute("DROP TABLE IF EXISTS orders;");
            logger.debug("Orders table was dropped");

            s.execute(
                    """
                        CREATE TABLE orders ( 
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            username VARCHAR(20) NOT NUll,
                            status ENUM('PENDING', 'SENT') NOT NULL,
                            packager_name VARCHAR(20),   

                            FOREIGN KEY (username) 
                                REFERENCES users (name)
                                ON DELETE CASCADE,

                            FOREIGN KEY (packager_name) 
                                REFERENCES users (name)
                         ) 
                         """);

            logger.debug("Orders table was created");
        } catch (SQLException e) {
            throw new RuntimeException("Can't create orders table", e);
        }
    }

    private void createOrdersAndProductsOneToManyTable() {
        try (Statement s = con.createStatement()) {
            s.execute("DROP TABLE IF EXISTS orders_products;");
            logger.debug("Orders_products table was dropped");

            s.execute(
                    """
                        CREATE TABLE orders_products (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            order_id INT NOT NULL,
                            product_id INT NOT NULL UNIQUE,
                        
                            FOREIGN KEY (order_id)
                                REFERENCES orders (id)
                                ON DELETE CASCADE,
                            
                            FOREIGN KEY (product_id)
                                REFERENCES products (id)
                                ON DELETE CASCADE
                        )
                         """);

            logger.debug("Orders and Products One to Many table was created");
        } catch (Exception e) {
            throw new RuntimeException(
                    "Can't create Orders and Products One to Many table", e);
        }
    }

    /**
     *
     * Creates an order and maps to it the products
     * To support this operation
     *  First: the order is created
     *  Second: using orders_products table and apllying OneToMany 
     *          relationship products are mapped to the order 
     *
     * @param username name of the user who creats the order 
     * @param products list of products add to the order
     * @param setAutoCommit sets the connection's autocommit state 
     * @return created order 
     */
    public OrderEntity createOrder(String username, List<ProductEntity> products, 
                                   boolean setAutoCommit) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(products); 
        
        if(products.isEmpty())
            throw new IllegalArgumentException("Products list cannot be empty");

        OrderEntity order = null;
        try {
            con.setAutoCommit(false);
            order = createOrder(username);
            mapOrderAndProducts(products, order);
        } catch(SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
               e.addSuppressed(e1);     
            }

           throw new RuntimeException("Can't create an order", e); 
        }finally {
            try {
                con.setAutoCommit(setAutoCommit);
            } catch (SQLException e) {
                throw new RuntimeException("Can't set autocommit to true", e); 
            }

        }

        return order;
    }


    /**
     * Creates an order   
     * 
     * @param username name of the use who created the order 
     * @return a newly created order
     */
    private OrderEntity createOrder(String username) {
        Objects.requireNonNull(username);

        String createOrderQueuery = 
                    """
                        INSERT INTO orders (username, status)
                        VALUES (?, ?)
                    """;

        OrderEntity order = null;

        try (PreparedStatement ps = 
                con.prepareStatement(createOrderQueuery, 
                                    RETURN_GENERATED_KEYS)) {

                ps.setString(1, username);
                ps.setString(2, OrderStatus.PENDING.name());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    order = new OrderEntity(
                               rs.getInt(1), 
                               username, 
                               null,
                               OrderStatus.PENDING); 
                }


        } catch (SQLException e) {
            throw new RuntimeException("Can't create the order", e);
        }

        logger.debug("Order was created for user {}", username);

        return order;
    }


    /**
     * Maps an existing order with existing products together to
     * form OneToMany relationship
     * 
     * NOTE: works in conjunction with {@link OrderRepository#createOrder(String)}
     * to faciliate OneToMany relationship 
     *
     * @param products a list of existiong products 
     * @param order an existtiong order
     */
    private void mapOrderAndProducts(List<ProductEntity> products, OrderEntity order) {
        String query = 
                    """
                        INSERT INTO orders_products (order_id, product_id)
                        VALUES (?, ?)
                    """;

        try (PreparedStatement ps = con.prepareStatement(query)) {
    
            for (ProductEntity product : products) {
                ps.setInt(1, order.getId());
                ps.setInt(2, product.getId());
                ps.executeUpdate();
            }

            logger.debug("Products were add to the order: {}", order );
        } catch (SQLException e) {
            throw new RuntimeException("Can't create order", e);
        }
    }

    public boolean existsById(int id) {
        String  query =
            """
                SELECT EXISTS(
                        SELECT 1 FROM orders 
                        WHERE id LIKE  ?
                        )
            """;

        boolean existsById = false;
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, id);
            try(ResultSet rs =  ps.executeQuery()) {
                rs.next();
                existsById = rs.getInt(1) ==  1;
            }
                
        } catch(SQLException e) {
            throw new RuntimeException("Can't find order with id" + id, e);
        } 

        logger.debug("Exists by id: {}  order {}", id, existsById); 
        return existsById; 
    }


    public List<OrderEntity> getAllOrdersByUsername(String username) {
        Objects.requireNonNull(username);

        String queuery = 
                        """
                        SELECT * FROM orders
                        WHERE username LIKE ? 
                        """;

        List<OrderEntity> orders = new ArrayList<>();
        
        try(PreparedStatement ps = con.prepareStatement(queuery)){
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(map(rs));
                }
            }
                
        } catch(SQLException e) {
            throw new RuntimeException(
                    "Can't find orders by username " + username, e);
        } 

        return orders;
    }

    public List<OrderEntity> getAllOrders() {
        String queuery = """
                        SELECT * FROM orders
                        """;

        List<OrderEntity> orders = new ArrayList<>();
        
        try(Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(queuery)) {
                while (rs.next()) 
                    orders.add(map(rs));
                
        } catch(SQLException e) {
            throw new RuntimeException(
                    "Can't find all orders", e);
        } 

        return orders;
    }
   
    public List<OrderEntity> getAllOrdersByStatus(OrderStatus status) {
        String queuery = """
                        SELECT * FROM orders
                        WHERE status LIKE ? 
                        """;

        List<OrderEntity> orders = new ArrayList<>();
        
        try(PreparedStatement ps = con.prepareStatement(queuery)){
            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(map(rs));
                }
            }
                
        } catch(SQLException e) {
            throw new RuntimeException(
                    "Can't find orders by status " + status, e);
        } 

        return orders;
    }

    public void packageOrder(int id, String packagerName) {
        String queuery = """
                        UPDATE orders
                            SET status = ?,
                                packager_name = ?        
                        WHERE id = ? 
                        """;
       

        try(PreparedStatement ps = con.prepareStatement(queuery)){
            ps.setString(1, OrderStatus.SENT.name());
            ps.setString(2, packagerName);
            ps.setInt(3, id);
            ps.executeUpdate();
                
        } catch(SQLException e) {
            throw new RuntimeException(
                    "Can't package order with id: " + id , e);
        } 
        logger.debug(
                "Order with id: {} was packaged by user with id{}", 
                    id, packagerName);
    }

    private static OrderEntity map(ResultSet rs) throws SQLException {
        return new OrderEntity(
                rs.getInt("id"), 
                rs.getString("username"),
                rs.getString("packager_name"),
                OrderStatus.valueOf(
                        rs.getString("status")));
    }


}
