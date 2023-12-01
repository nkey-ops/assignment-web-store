package ui.models;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Pending"), 
    SENT("Sent");

    public final String label;

    private OrderStatus(String label) {
        this.label = label;
    }
}
