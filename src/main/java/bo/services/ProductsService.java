package bo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import db.entities.ProductEntity;
import db.entities.ShoppingCartEntity;
import db.repositories.ProductsRepository;
import ui.models.ProductModel;

/**
 * ProductService
 */
public class ProductsService {
    private final ProductsRepository productsRepo;
    private final UsersService usersService;

    public ProductsService(ProductsRepository productsRepo,
            UsersService usersService) {
        Objects.requireNonNull(productsRepo);
        Objects.requireNonNull(usersService);

        this.productsRepo = productsRepo;
        this.usersService = usersService;
    }

    public List<ProductModel> createProducts(String name, double price,
            int quantity, String category) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(category);
        if (price <= 0 || quantity <= 0)
            throw new IllegalArgumentException();

        List<ProductModel> productModels = new ArrayList<>();
        for (ProductEntity productEntity : productsRepo.create(name, price, quantity, category)) {
            productModels.add(map(productEntity));
        }

        return productModels;
    }

    public void updateProduct(ProductModel product) {
        Objects.requireNonNull(product);

        if (!productsRepo.existsById(product.getId()))
            throw new IllegalArgumentException(
                    "Product with id: " + product.getId() + " wasn't found");

        productsRepo.update(map(product));
    }

    public ProductModel getProduct(int id) {
        return map(productsRepo.getProductById(id));
    }

    public List<ProductModel> getAllProducts() {
        List<ProductModel> productModels = new ArrayList<>();
        for (ProductEntity productEntity : productsRepo.getAllProducts()) {
            productModels.add(map(productEntity));
        }

        return productModels;
    }

    public List<ProductModel> getProductsNotChosenByUser(String username) {
        Objects.requireNonNull(username);

        List<ProductModel> productModels = new ArrayList<>();
        for (ProductEntity productEntity : productsRepo.getProductsNotChosenByUser(username)) {
            productModels.add(map(productEntity));
        }

        return productModels;
    }

    public List<ProductModel> getAllProductsByOrderId(int orderId) {
        List<ProductModel> productModels = new ArrayList<>();
        for (ProductEntity productEntity : productsRepo.getAllProductsByOrderId(orderId)) {
            productModels.add(map(productEntity));
        }

        return productModels;
    }

    /**
     * @param name of the user
     * @return list of {@link ProductModel}'s that are
     *         presen't in user's {@link ShoppingCartEntity}
     */
    public List<ProductModel> getProductsByUser(String name) {
        Objects.requireNonNull(name);

        if (!usersService.existsUser(name))
            throw new IllegalArgumentException("User wasn't found");

        List<ProductModel> productModels = new ArrayList<>();
        for (ProductEntity productEntity : productsRepo.getAllProductsByUsername(name)) {
            productModels.add(map(productEntity));
        }

        return productModels;
    }

    static ProductModel map(ProductEntity productEntity) {
        Objects.requireNonNull(productEntity);

        return new ProductModel(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getStatus(),
                productEntity.getCategory());
    }

    static ProductEntity map(ProductModel productModel) {
        Objects.requireNonNull(productModel);

        return new ProductEntity(
                productModel.getId(),
                productModel.getName(),
                productModel.getPrice(),
                productModel.getStatus(),
                productModel.getCategory());
    }

}
