package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.repositories.CategoriesRepository;
import db.repositories.OrderRepository;
import db.repositories.ProductsRepository;
import db.repositories.ShoppingCartRepository;
import db.repositories.UsersRepository;
import lombok.Getter;

public class DBManager implements AutoCloseable {
  private static Logger logger = LogManager.getLogger();

  private static DBManager instance = null;
  @Getter private Connection connection = null;

  @Getter private UsersRepository usersRepo;
  @Getter private final ProductsRepository productsRepo;
  @Getter private final ShoppingCartRepository shoppingCartRepo;
  @Getter private final OrderRepository orderRepo;
  @Getter private final CategoriesRepository categoriesRepo;

  private final String user = "nik";
  private final String password = "pass";
  private final String database = "workshopedb";
  private final String server = "jdbc:mysql://localhost:3306/" + database + "?UseClientEnc=UTF8";

  private DBManager() {
    createConnection();
    logger.debug("Connection was created");

    try (Statement s = connection.createStatement()) {
      connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");

      this.usersRepo = new UsersRepository(connection);
      logger.debug("{} was created", UsersRepository.class.getSimpleName());

      this.productsRepo = new ProductsRepository(connection);
      logger.debug("{} was created", ProductsRepository.class.getSimpleName());

      this.shoppingCartRepo = new ShoppingCartRepository(connection);
      logger.debug("{} was created", ShoppingCartRepository.class.getSimpleName());

      this.orderRepo = new OrderRepository(connection);
      logger.debug("{} was created", OrderRepository.class.getSimpleName());

      this.categoriesRepo = new CategoriesRepository(connection);
      logger.debug("{} was created", CategoriesRepository.class.getSimpleName());

      connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    logger.info("{} was initialized", DBManager.class.getSimpleName());
  }

  public static DBManager getInstance() {
    if (instance == null) instance = new DBManager();
    return instance;
  }

  public void close() {
    try {
      instance = null;
      connection.close();
      logger.info("Connection was closed");
    } catch (SQLException e) {
      logger.error(
          () -> "Unable to close database connection on contex destroy", e);
    }
  }

  private void createConnection() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection(server, user, password);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
