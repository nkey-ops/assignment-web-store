package ui.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ProductModel
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductModel {
    private int id;
    private String name;
    private double price;
    private ProductStatus status; 
    private String category;
}
