import java.util.Map;
import java.util.HashMap;

public class BeanCard {
    private final BeanCardType type; // Bean type cannot change after creation
    private final int value;        // Bean value cannot change
    private int quantity;           // Quantity can change during gameplay

    public BeanCard(BeanCardType type) {
        this.type = type;
        this.value = type.calculateScore(1); // Get the value for a single bean of this type
        this.quantity = 1; // Initially, a bean card represents a single bean
    }
    
    // Getters for immutable attributes
    public BeanCardType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    // Getter and setter for mutable attribute
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%d x %s (%d gold)", quantity, type.getName(), value);
    }
}
