package db.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a details of an item in a shopping cart, including user ID, item ID, and quantity.
 */
@Getter
@Setter
public class ShoppingCartEntity {
    private int userid;
    private int itemid;
    private int quantity;
    private Status status; 

    public static enum Status {
        ACTIVE("Active"), 
        ORDERED("Ordered");

        private final String label;

        private Status(String label) {
            this.label = label;
        }
    }

    /**
     * Constructs a ShoppingCart entry with the given user ID, item ID, and quantity.
     *
     * @param userid   the ID of the user who owns the shopping cart entry
     * @param itemid   the ID of the item in the shopping cart entry
     * @param quantity the quantity of the item in the shopping cart entry
     */
    public ShoppingCartEntity(int userid, int itemid, int quantity) {
        this.userid = userid;
        this.itemid = itemid;
        this.quantity = quantity;
        this.status = Status.ACTIVE;
    }
}
