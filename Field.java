import java.util.ArrayList;
import java.util.List;

public class Field {
    private List<BeanCard> beans;
    private BeanCardType type; // Current type of beans in the field (null if empty)

    public Field() {
        this.beans = new ArrayList<>();
        this.type = null; // Initially empty
    }

    // Getters
    public List<BeanCard> getBeans() {
        return beans;
    }

    public BeanCardType getType() {
        return type;
    }

    // Methods
    public void addBeanCard(BeanCard card) {
        if (beans.isEmpty()) {
            // This is the first bean planted, so set the field type
            type = card.getType();
        } else if (!type.equals(card.getType())) {
            // Cannot plant a different type of bean in this field
            System.out.println("Error: You can only plant " + type.getName() + " in this field.");
            return;
        }

        // If there's already a bean card of this type, increase the quantity instead of adding a new card
        for (BeanCard existingCard : beans) {
            if (existingCard.getType() == card.getType()) {
                existingCard.setQuantity(existingCard.getQuantity() + 1);
                return;
            }
        }

        // If no matching card is found, add the new card
        beans.add(card);
    }

    public void removeBeanCard(BeanCard card) {
        if (beans.remove(card)) {
            // If the last bean card is removed, reset the field type to null
            if (beans.isEmpty()) {
                type = null;
            }
        } else {
            System.out.println("Error: Card not found in field.");
        }
    }

    public void removeBeanCards(BeanCardType type) {
        final BeanCardType finalType = type; // Create a final copy
        beans.removeIf(card -> card.getType() == finalType); // Use the final copy in the lambda

        // If the field is now empty, reset the field type to null
        if (beans.isEmpty()) {
            this.type = null;
        }
    }


    public int getNumberOfBeans(BeanCardType type) {
        int count = 0;
        for (BeanCard card : beans) {
            if (card.getType() == type) {
                count += card.getQuantity();
            }
        }
        return count;
    }

    public int calculateScore() {
        if (beans.isEmpty()) {
            return 0; // Empty field has no score
        }
        return type.calculateScore(getNumberOfBeans(type));
    }

    public boolean canPlantBean(BeanCardType typeToPlant) {
        return beans.isEmpty() || typeToPlant == type; // Allow planting if empty or matching type
    }

    @Override
    public String toString() {
        if (beans.isEmpty()) {
            return "Empty field";
        } else {
            return beans.toString(); // Use the toString of the BeanCard list for display
        }
    }
}
