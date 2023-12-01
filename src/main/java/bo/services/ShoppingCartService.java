package bo.services;

import java.util.Objects;

import db.repositories.ProductsRepository;
import db.repositories.ShoppingCartRepository;
import db.repositories.UsersRepository;

/**
 * ShoppingCartService
 */
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepo;
    private final UsersRepository usersRepo;
    private final ProductsRepository productsRepo;
    
    public ShoppingCartService(
                            ShoppingCartRepository shoppingCartRepo,
                            UsersRepository usersRepo,
                            ProductsRepository productsRepo  ) {
        Objects.requireNonNull(shoppingCartRepo);
        Objects.requireNonNull(usersRepo);
        Objects.requireNonNull(productsRepo);

        this.shoppingCartRepo = shoppingCartRepo;
        this.usersRepo = usersRepo;
        this.productsRepo = productsRepo;
     }

    /**
     * Adds to user's cart the product 
     * @param username of the cart owner 
     * @param id of the product to add
     */
    public void addProduct(String username, int id) {
        Objects.requireNonNull(username);
        if(!usersRepo.existsByUsername(username))
            throw new IllegalArgumentException("User wasn't found");  

        if(!productsRepo.existsById(id))
            throw new IllegalArgumentException("Product wasn't found");  

        shoppingCartRepo.addProduct(username, id); 
    }

    /**
     * Adds to user's cart the product 
     * @param username of the cart owner 
     * @param id of the product to remove
     */
    public void removeProduct(String username, int id) {
        Objects.requireNonNull(username);
        if(!usersRepo.existsByUsername(username))
            throw new IllegalArgumentException("User wasn't found");  

        if(!productsRepo.existsById(id))
            throw new IllegalArgumentException("Product wasn't found");  

        shoppingCartRepo.removeProduct(username, id);
    }

    /**
     * Deletes all the products from the user's shopping cart
     * @param username  of the cart owner
     */
    public void removeAll(String username) {
        Objects.requireNonNull(username);
        if(!usersRepo.existsByUsername(username))
            throw new IllegalArgumentException("User wasn't found");  

        shoppingCartRepo.removeAll(username);
    }




}
