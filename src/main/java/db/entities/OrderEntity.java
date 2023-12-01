package db.entities;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ui.models.OrderStatus;

/**
 * Order
 */
@Getter
@Setter
@ToString
public class OrderEntity {
    private int id;
    private String buyerName;
    private String packagerName;
    private OrderStatus status;

    public OrderEntity(int id, String buyerName, 
                        String packagerName,
                        OrderStatus status) {
        Objects.requireNonNull(status);
        Objects.requireNonNull(buyerName);

        this.id = id;
        this.status = status;
        this.buyerName = buyerName;
        this.packagerName = packagerName;
    }


}

