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

import db.entities.ProductEntity;
import ui.models.ProductStatus;

/**
 * This calss is to manages database operations related to products.
 */
public class ProductsRepository {
    private static Logger logger = LogManager.getLogger();
    private final Connection con;

    public ProductsRepository(Connection con) {
        this.con = con;

        createItemsTableOrReplace();
    }

    private void createItemsTableOrReplace() {
        try (Statement s = con.createStatement()) {
            s.execute("DROP TABLE IF EXISTS products;");
            logger.debug("Products  table was dropped");
            s.execute(
                    """
                            CREATE TABLE products  (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                name VARCHAR(15) NOT NULL,
                                price INT NOT NULL,
                                category VARCHAR(15),
                                status ENUM('ACTIVE', 'SOLD') NOT NULL,

                                FOREIGN KEY (category)
                                    REFERENCES categories (name)

                            )
                            """);

            logger.debug("Products table was created");
        } catch (Exception e) {
            throw new RuntimeException("Can't create user table", e);
        }
    }

    public List<ProductEntity> create(String name, double price,
            int qty, String category) {
        if (name == null || category == null
                || price < 0 || qty < 0)
            throw new IllegalArgumentException();

        List<ProductEntity> products = new ArrayList<>();
        String query = """
                  INSERT INTO products (name, price, category)
                  VALUE (?, ?, ?);
                """;

        try (PreparedStatement ps = con.prepareStatement(query,
                RETURN_GENERATED_KEYS)) {

            while (qty != 0) {
                ps.setString(1, name);
                ps.setDouble(2, price);
                ps.setString(3, category);
                ps.addBatch();
                qty--;
            }

            ps.executeBatch();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                while(rs.next())
                    products.add(
                            new ProductEntity(
                                    rs.getInt(1), name, price,
                                    ProductStatus.ACTIVE,
                                    category));

            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't create a product ", e);
        }

        logger.debug(
                "Created products: name - {} price - {} quantity - {}",
                name, price, products.size());

        return products;
    }

    /**
     * Updates the product found by id
     * with name, price, status and category
     * data retrived from thr provided product
     *
     * @param product
     */
    public void update(ProductEntity product) {
        Objects.requireNonNull(product);

        String query = """
                 UPDATE products
                 SET name = ?,
                     price = ?,
                     status = ?,
                     category = ?
                 WHERE id = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getStatus().name());
            ps.setString(4, product.getCategory());
            ps.setInt(5, product.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Can't update product: " + product, e);
        }

        logger.debug("Product was updated: " + product);

    }

    public boolean removeById(int pid) {
        String queury = """
                UPDATE products
                SET quantity = quantity - 1
                WHERE id = 1 AND quantity != 0
                """;

        try (Statement s = con.createStatement()) {
            int i = s.executeUpdate(queury);
            logger.debug(
                    "Product was removed id: {} {}", pid, i != 0);

            return i != 0;
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    public List<ProductEntity> getProductsNotChosenByUser(String username) {
        String queury = """
                    SELECT * FROM products
                    WHERE 0 = (
                        SELECT COUNT(*)
                        FROM shopping_carts AS sc
                        WHERE products.id = sc.product_id
                           AND sc.username LIKE ?
                    ) AND products.status NOT LIKE 'SOLD' 
                """;
        List<ProductEntity> products = new ArrayList<>();

        try (PreparedStatement s = con.prepareStatement(queury)) {
            s.setString(1, username);
            ResultSet rSet = s.executeQuery();

            while (rSet.next())
                products.add(map(rSet));

        } catch (SQLException e) {
            throw new RuntimeException("Can't retrieve all not chosen products ", e);
        }

        logger.debug("Products not chosen by user: {} were returned", username);

        return products;
    }

    /**
     * @param orderId id of the order
     * @return a list of {@link ProductEntity}'s that is included in the order
     */
    public List<ProductEntity> getAllProductsByOrderId(int orderId) {
        String queuery = """
                SELECT p.* FROM products AS p
                JOIN orders_products AS op
                   ON p.id = op.product_id
                   AND op.order_id = ?
                """;

        List<ProductEntity> products = new ArrayList<>();

        try (PreparedStatement s = con.prepareStatement(queuery)) {
            s.setInt(1, orderId);

            try (ResultSet rSet = s.executeQuery();) {
                while (rSet.next())
                    products.add(map(rSet));

            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't retrieve all the products by order id", e);
        }

        return products;
    }

    /**
     * 
     * @param username
     * @return a list of active products
     */
    public List<ProductEntity> getAllProductsByUsername(String username) {
        Objects.requireNonNull(username);
        String queury = """
                    SELECT products.*
                    FROM products
                    INNER JOIN shopping_carts as sc
                        ON  sc.product_id = products.id
                        AND sc.username = ?
                    WHERE products.status LIKE ?
                """;

        List<ProductEntity> products = new ArrayList<>();
        try (PreparedStatement s = con.prepareStatement(queury)) {
            s.setString(1, username);
            s.setString(2, ProductStatus.ACTIVE.name());

            try (ResultSet rSet = s.executeQuery()) {
                while (rSet.next())
                    products.add(map(rSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Can't get all the products by username", e);
        }

        logger.debug(
                "All active products by user {} were returned", username);

        return products;
    }

    public List<ProductEntity> getAllProducts() {
        String queuery = """
                SELECT * FROM products
                """;

        List<ProductEntity> products = new ArrayList<>();

        try (Statement s = con.createStatement();
                ResultSet rs = s.executeQuery(queuery)) {

            while (rs.next())
                products.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Can't retrieve all the products", e);
        }

        logger.debug("All the producs were retrieved");

        return products;
    }

    public boolean existsById(int id) {
        String query = """
                    SELECT EXISTS(
                            SELECT 1 FROM products
                            WHERE id LIKE  ?
                            )
                       """;

        boolean existsById = false;
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                existsById = rs.getInt(1) == 1;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Can't find product with id" + id, e);
        }

        logger.debug("Product with id:{} is found: {} ", id, existsById);
        return existsById;
    }

    public ProductEntity getProductById(int id) {
        String query = """
                    SELECT * FROM products
                    WHERE id = ?
                """;

        ProductEntity product = null;
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                product = map(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Can't find product with id" + id, e);
        }

        logger.debug("Product was found by id ", product);
        return product;
    }

    private static ProductEntity map(ResultSet rs) throws SQLException {
        return new ProductEntity(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                ProductStatus.valueOf(
                        rs.getString("status")),
                rs.getString("category"));
    }

}
