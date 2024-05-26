import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

public class Deck {
    private List<BeanCard> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        initializeDeck();
        shuffleCards();
    }

    private void initializeDeck() {
        addCardsToDeck(BeanCardType.STINK_BEAN, 12);
        addCardsToDeck(BeanCardType.BLUE_BEAN, 10);
        addCardsToDeck(BeanCardType.CHILI_BEAN, 8);
        addCardsToDeck(BeanCardType.GREEN_BEAN, 8);
        addCardsToDeck(BeanCardType.SOY_BEAN, 7);
        addCardsToDeck(BeanCardType.BLACK_EYED_BEAN, 6);
        addCardsToDeck(BeanCardType.RED_BEAN, 5);
        addCardsToDeck(BeanCardType.WAX_BEAN, 4);
        addCardsToDeck(BeanCardType.GARDEN_BEAN, 3);
        addCardsToDeck(BeanCardType.COCOA_BEAN, 2);
        addCardsToDeck(BeanCardType.COFFEE_BEAN, 2);
    }

    private void addCardsToDeck(BeanCardType type, int count) {
        for (int i = 0; i < count; i++) {
            cards.add(new BeanCard(type));
        }
    }

    public void shuffleCards() {
        Collections.shuffle(cards);
    }

    public BeanCard drawCard() {
        if (cards.isEmpty()) {
            return null; // Or throw an exception, depending on your preference
        }
        return cards.remove(0); // Remove and return the top card
    }
}
