package db.entities;


import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import ui.models.UserModel;
import ui.models.UserModel.Type;

/**
 * Represents a user with an ID, name and type.
 */
@Getter
@Setter
public class UserEntity {
    private int id;
    private String name;
    private String password;
    private UserModel.Type type;

    /**
     * @param id userId
     * @param name username
     * @param string
     * @param type user type 
     */
    public UserEntity(int id, String name, 
                        String password, Type type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        Objects.requireNonNull(password);

        this.id = id;
        this.name = name;
        this.type = type;
        this.password = password;
    }
}
