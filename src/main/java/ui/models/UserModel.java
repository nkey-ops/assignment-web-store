package ui.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserModel {
    private int id; 
    private String name; 
    private Type type; 

    public enum Type {
        ADMIN("Admin"), 
        CLIENT("Client"), 
        WAREHOUSE_STAFF("Warehouse Staff"); 

        public final String label;

        private Type(String label) {
            this.label = label;
        }
    }
}

