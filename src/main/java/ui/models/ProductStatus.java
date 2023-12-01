package ui.models;

public enum ProductStatus {
    ACTIVE("Active"), 
    SOLD("Sold");

    public final String label;

    private ProductStatus(String label) {
        this.label = label;
    }
}
