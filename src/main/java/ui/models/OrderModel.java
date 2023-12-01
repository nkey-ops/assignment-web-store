package ui.models;

import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Order
 */
@Getter
@Setter
@ToString
public class OrderModel {
    private int id;
    private String buyerName;
    private String packagerName;
    private OrderStatus status; 
    private List<ProductModel> products;

    public OrderModel(int id, String buyerName, 
                 String packagerName,
                 OrderStatus status, 
                 List<ProductModel> products) {
        Objects.requireNonNull(buyerName);
        Objects.requireNonNull(status);
        Objects.requireNonNull(products);
    
        this.id = id;
        this.buyerName = buyerName;
        this.packagerName = packagerName;
        this.status = status;
        this.products = products;
    }
}

    
