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

import db.entities.UserEntity;
import ui.models.UserModel;
import ui.models.UserModel.Type;

/**
 * Manages database operations related to users.
 */
public class UsersRepository {
    private static Logger logger = LogManager.getLogger();
    private final Connection con;

    public UsersRepository(Connection con) {
        this.con = con;
        createUserTable();
    }

    /**
     * Drops users table if exists and creates a new one
     * 
     */
    private void createUserTable() {
        try (Statement s = con.createStatement()) {
            s.execute("DROP TABLE IF EXISTS users;");

            logger.debug("User table was dropped");
            s.execute(
                    """
                            CREATE TABLE IF NOT EXISTS users (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(15) NOT NULL UNIQUE,
                            password VARCHAR(20) NOT NULL,
                            type VARCHAR(20) NOT NULL);
                            """);

            logger.debug("User table was created");
        } catch (Exception e) {
            throw new RuntimeException("Can't create user table", e);
        }
    }

    /**
     * Registers a new user with the specified username and password.
     *
     * @param username The username of the new user
     * @param password The password of the new user
     * @return User object of the newly registered user
     * @throws SQLException If there's an issue during the database operation
     */
    public UserEntity register(String username, String password, Type userType) {
        UserEntity res = null;
        String query = """
                   INSERT INTO users (name, password, type)
                   VALUE (?,?,?);
                   """;
        try (PreparedStatement pstmt = 
                    con.prepareStatement(query, RETURN_GENERATED_KEYS)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, userType.name());
            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys())  {
                rs.next();
                res = new UserEntity(rs.getInt(1), username, 
                                     password, userType);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        
        logger.debug("User with username {} was registered", username);

        return res;
    }


    /**
     * @param username used to search for a user  
     * @return wheather a users exists with such a username 
     */
    public boolean existsByUsername(String username) {
        Objects.requireNonNull(username);
        String  query =
            """
                SELECT EXISTS(
                        SELECT 1 FROM users 
                        WHERE name LIKE  ?
                        )
            """;

        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, username);
            try(ResultSet rs =  ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) ==  1;
            }
                
        } catch(SQLException e) {
            throw new RuntimeException("Can't find user " + username, e);
        } 
    }

    /**
     * @return All the users
     */
    public List<UserEntity> getAllUsers() {
        String queuery = 
            """
                SELECT * FROM users
            """;

        List<UserEntity> users = new ArrayList<>();
        
        try(Statement s = con.createStatement();
            ResultSet rs =  s.executeQuery(queuery)) {
            while(rs.next())
                users.add(map(rs));

        } catch(SQLException e) {
            throw new RuntimeException("Can't retrieve all users", e);
        } 
        logger.debug("All users were returned");

        return users;
    }


    /**
     * Updates user's {@link Type} with a new one 
     *
     * @param username to find the user
     * @param type     to replace previous {@link Type}
     */
    public void updateUserType(String username, Type type) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(type);
    
        String queuery = """ 
                            UPDATE users
                            SET type = ?
                            WHERE name = ?
                         """;

        try(PreparedStatement ps = con.prepareStatement(queuery)){
            ps.setString(1, type.name()); 
            ps.setString(2, username); 

            if(ps.executeUpdate() == 0);
                new RuntimeException("Couldn't update the user with username: " +  username) ;

            
        } catch(SQLException e) {
            throw new RuntimeException("Can't update the user", e);
        }
    }

    public UserEntity getUserByName(String username) {
        Objects.requireNonNull(username);
        String queuery = 
            """
                SELECT * FROM users
                WHERE name LIKE ?
            """;

        UserEntity userEntity;
        try(PreparedStatement ps = con.prepareStatement(queuery)) {
            ps.setString(1, username); 

           try(ResultSet rs =  ps.executeQuery()) {
               rs.next();
              userEntity =  map(rs);
           } 
        } catch(SQLException e) {
            throw new RuntimeException("Can't retrieve all users", e);
        } 

        logger.debug("User was returned by name: {}",  username);
        
        return userEntity;
    }

    private static UserEntity map(ResultSet rs) throws SQLException {
        return new UserEntity(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("password"),
                            UserModel.Type.valueOf(
                                rs.getString("type")));
    }

}
