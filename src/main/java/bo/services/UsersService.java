package bo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import db.entities.UserEntity;
import db.repositories.UsersRepository;
import ui.models.UserModel;
import ui.models.UserModel.Type;

/**
 * UserService
 */
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> users = new ArrayList<>();
        for (UserEntity user : usersRepository.getAllUsers()) {
            users.add(
                    new UserModel(user.getId(),
                            user.getName(),
                            user.getType()));
        }

        return users;
    }

    public void updateUserType(String username, Type type) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(type);

        if (!usersRepository.existsByUsername(username))
            throw new IllegalArgumentException("User wasn't found");

        usersRepository.updateUserType(username, type);
    }

    public boolean checkCredentials(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        if (!usersRepository.existsByUsername(username))
            return false;

        UserEntity user = usersRepository.getUserByName(username);
        return user.getPassword().equals(password);
    }

    public UserModel getUserByName(String username) {
        UserEntity user = usersRepository.getUserByName(username);
        return map(user);
    }

    private static UserModel map(UserEntity userEntity) {
        Objects.requireNonNull(userEntity);

        return new UserModel(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getType());
    }

    public void createUser(String username, String password) {
        if (usersRepository.existsByUsername(username))
            throw new IllegalArgumentException(
                    "User already exists");

        usersRepository.register(username, password, UserModel.Type.CLIENT);
    }

    public boolean existsUser(String name) {
        Objects.requireNonNull(name);
        return usersRepository.existsByUsername(name);
    }

}
