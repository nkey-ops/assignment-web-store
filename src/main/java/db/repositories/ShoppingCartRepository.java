package db.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Objects;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.entities.ShoppingCartEntity;
import ui.models.ProductStatus;

/**
 * Manages database operations related to shopping carts.
 */
public class ShoppingCartRepository {
    private static Logger logger = LogManager.getLogger();
    private final Connection con;

    public ShoppingCartRepository(Connection con) {
        this.con = con;

        createShoppingCartTableOrReplace();
    }

    private void createShoppingCartTableOrReplace() {
        try (Statement s = con.createStatement()) {
            s.execute("DROP TABLE IF EXISTS shopping_carts;");
            logger.debug("Shopping carts table was dropped");

            s.execute(
                    """
                            CREATE TABLE shopping_carts (
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    username VARCHAR(15) NOT NULL,
                                    product_id  int NOT NULL,
                                    status ENUM('ACTIVE', 'ORDERED') NOT NULL,

                                    FOREIGN KEY (username)
                                        REFERENCES users (name)
                                         ON DELETE CASCADE,

                                    FOREIGN KEY (product_id)
                                         REFERENCES products (id)
                                         ON DELETE CASCADE
                            )
                                """);

            logger.debug("Shopping carts table was created");
        } catch (Exception e) {
            throw new RuntimeException("Can't create user table", e);
        }
    }


    /**
     * Removes all items from the user's shopping cart.
     *
     * @param username of the cart owner
     */
    public void removeAll(String username) {
        Objects.requireNonNull(username);

        String query = """
                  DELETE FROM shopping_carts
                  WHERE username LIKE ?
                """;

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Can't remove all the items for user's cart", e);
        }

        logger.debug("All items were removed for user: {}", username);
    }

    /**
     * Adds the item to the shopping cart
     *
     * @param username of the cart owner
     * @param id       of the product to add to the cart
     */
    public boolean addProduct(String username, int id) {
        Objects.requireNonNull(username);

        String query = """
                  INSERT INTO shopping_carts (username, product_id, status)
                  VALUE (?, ?, ?)
                """;

        try (PreparedStatement s = con.prepareStatement(query)) {
            s.setString(1, username);
            s.setInt(2, id);
            s.setString(3, ProductStatus.ACTIVE.name());

            boolean isAdded = s.executeUpdate() != 0;

            logger.debug(
                    "Product with id:{} added to {}'s cart",
                    id, username);

            return isAdded;

        } catch (SQLException e) {
            throw new RuntimeException("Can't insert a Product", e);
        }
    }

    /**
     * Removed the product from the shopping cart
     *
     * @param username of the cart owner
     * @param id       of the product to remove
     */
    public void removeProduct(String username, int id) {
        Objects.requireNonNull(username);

        String query = """
                 DELETE FROM shopping_carts
                 WHERE username LIKE ? AND
                       product_id = ?
                """;

        try (PreparedStatement s = con.prepareStatement(query)) {
            s.setString(1, username);
            s.setInt(2, id);
            s.executeUpdate();

            logger.debug(
                    "Product with id:{} was removed from {}'s cart",
                    id, username);

        } catch (SQLException e) {
            throw new RuntimeException("Can't remove the product", e);
        }
    }

}
