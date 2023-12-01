package db.repositories;

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


/**
 * CategoriesRepository
 */
public class CategoriesRepository {
    private static Logger logger = LogManager.getLogger();
    private final Connection con;

    public CategoriesRepository(Connection con) {
        this.con = con;

        createCategoriesTable();
    }

    private void createCategoriesTable() {
        try (Statement s = con.createStatement()) {
            s.execute("DROP TABLE IF EXISTS categories");
            logger.debug("Categories table was dropped");

            s.execute(
                    """
                        CREATE TABLE categories ( 
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(15) UNIQUE 
                        )
                    """);

            logger.debug("Categories table was created");
        } catch (SQLException e) {
            throw new RuntimeException("Can't create categorie stable", e);
        }
    }

    
    public List<String> getAllCategories() {
        String queuery = "SELECT * FROM categories";
        List<String> categories = new ArrayList<>();

        try (Statement s = con.createStatement();
              ResultSet rs = s.executeQuery(queuery) ) {
          
            while(rs.next())
                categories.add(
                        rs.getString("name"));

        } catch (SQLException e) {
            throw new RuntimeException("Can't create categorie stable", e);
        }

        return categories;
    }

    /**
     * Creates a category with a privide name
     * 
     * @param categoryName the name of the category to create
     */
    public void createCategory(String categoryName) {
        Objects.requireNonNull(categoryName);

        String queuery = """
                            INSERT INTO categories (name)
                            VALUES (?)
                         """;

        try (PreparedStatement ps = con.prepareStatement(queuery)) {
            ps.setString(1, categoryName);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Can't create the category", e);
        }
    }

    /**
     * Checks wheather the catergory exists 
     * @param categoryName the name of the category to check
     * @return wheather the category exists 
     */
    public boolean existsByName(String categoryName ) {
        Objects.requireNonNull(categoryName);

        String queuery = """
                            SELECT EXISTS(
                                    SELECT 1 FROM categories 
                                    WHERE name LIKE  ?
                            )
                         """;
        boolean existsByName = false;
        try (PreparedStatement ps = con.prepareStatement(queuery)) {
            ps.setString(1, categoryName);

            try(ResultSet rs =  ps.executeQuery()) {
                rs.next();
                existsByName = rs.getInt(1) ==  1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Can't check whether the category exists", e);
        }


        logger.debug("Does the Category: {}  exists: {}",
                                categoryName, existsByName);
        return existsByName; 
    }
    
}
