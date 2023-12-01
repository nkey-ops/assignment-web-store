package db.entities;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ui.models.ProductStatus;

@Getter
@Setter
@ToString
public class ProductEntity {
    private int id;
    private String name;
    private double price;
    private ProductStatus status; 
    private String category;

    public ProductEntity(int id, String name, 
                   double price, 
                   ProductStatus status,
                   String category) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(status);
        Objects.requireNonNull(category);

        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
        this.category = category;
    }
}
