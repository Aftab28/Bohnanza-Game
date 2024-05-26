import java.util.*;

public class Player {
    private String name;
    private List<BeanCard> hand;
    private List<Field> fields;
    public int score;
    private int coins;
    private Game game; 

    public Player(String name, Game game) {
        this.name = name;
        this.game = game;
        this.hand = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.score = 0;
        this.coins = 3; // Starting coins (can be adjusted based on the game rules)

        for (int i = 0; i < 2; i++) { // Initial two fields
            fields.add(new Field());
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<BeanCard> getHand() {
        return new ArrayList<>(hand); // Return a copy to protect the internal state
    }

    public List<Field> getFields() {
        return new ArrayList<>(fields); // Return a copy to protect the internal state
    }

    public int getScore() {
        return score;
    }

    public int getCoins() {
        return coins;
    }

    // Actions
    public void drawCard(BeanCard card) {
        hand.add(card);
        System.out.println(name + " drew: " + card);
    }

    public void plantBeanCard(Field field, BeanCard card) {
        if (field.canPlantBean(card.getType())) {
            hand.remove(card);
            field.addBeanCard(card);
            System.out.println(name + " planted " + card + " in field " + (fields.indexOf(field) + 1));
        } else {
            System.out.println("You cannot plant this bean type in this field.");
            discardBeanCard(card); 
        }
    }

    public void harvestBeans(Field field) {
        if (field.getBeans().isEmpty()) {
            System.out.println("This field is empty. Nothing to harvest.");
            return;
        }

        BeanCardType type = field.getBeans().get(0).getType();
        int numBeans = field.getNumberOfBeans(type);
        int harvestValue = type.calculateScore(numBeans);

        score += harvestValue;
        coins += harvestValue;

        field.removeBeanCards(type);
        fields.remove(field);
        fields.add(new Field()); 

        System.out.println(name + " harvested " + numBeans + " " + type.getName() + 
                           " and earned " + harvestValue + " gold. Total coins: " + coins);
    }

    public List<BeanCard> selectCardsForTrade(Scanner scanner) {
        System.out.println("Your hand: ");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". " + hand.get(i));
        }
        
        System.out.print("Enter the indices of the cards you want to offer (comma-separated), or '0' for none: ");
        String input = scanner.nextLine();

        if (input.equals("0")) {
            return new ArrayList<>(); // No cards offered
        }

        List<BeanCard> offeredCards = new ArrayList<>();
        try {
            for (String indexStr : input.split(",")) {
                int index = Integer.parseInt(indexStr.trim()) - 1; // Adjust for 1-based indexing
                if (index >= 0 && index < hand.size()) {
                    offeredCards.add(hand.get(index));
                } else {
                    System.out.println("Invalid card index: " + (index + 1));
                    return selectCardsForTrade(scanner); // Ask for input again
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format.");
            return selectCardsForTrade(scanner); // Ask for input again
        }
        return offeredCards;
    }

    public Player selectTradingPartner(List<Player> players, Scanner scanner) {
        System.out.println("Other players:");
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != this) {
                System.out.println((i + 1) + ". " + players.get(i).getName());
            }
        }
        
        System.out.print("Enter the number of the player you want to trade with (or 0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 0) {
            return null;
        }

        int playerIndex = choice - 1;
        if (playerIndex >= 0 && playerIndex < players.size() && players.get(playerIndex) != this) {
            return players.get(playerIndex);
        } else {
            System.out.println("Invalid player choice.");
            return selectTradingPartner(players, scanner); 
        }
    }
    
    private int calculateTotalValue(List<BeanCard> cards) {
        int totalValue = 0;
        for (BeanCard card : cards) {
            totalValue += card.getValue() * card.getQuantity(); // Calculate the value for each card based on quantity
        }
        return totalValue;
    }
    
    public boolean acceptTrade(Trade trade) {
        Player otherPlayer = (trade.getPlayer1() == this) ? trade.getPlayer2() : trade.getPlayer1();
        List<BeanCard> offeredCards = trade.getOfferedCards(otherPlayer);
        List<BeanCard> myCards = trade.getOfferedCards(this);

        int offerValue = calculateTotalValue(offeredCards);
        int myOfferValue = calculateTotalValue(myCards);
        
        System.out.println(otherPlayer.getName() + " offers: " + offeredCards +
                            " (Value: " + offerValue + ")");
        System.out.println("You offered: " + myCards + 
                            " (Value: " + myOfferValue + ")");
        System.out.print("Do you accept this trade? (y/n): ");
        String choice = game.getScanner().nextLine().toLowerCase();

        if (choice.equals("y")) {
            // Check if the player has enough coins
            if (coins >= offerValue - myOfferValue) { 
                hand.removeAll(myCards);
                hand.addAll(offeredCards);
                coins -= (offerValue - myOfferValue);
                otherPlayer.coins += (offerValue - myOfferValue);
                return true;
            } else {
                System.out.println("You don't have enough coins for this trade.");
            }
        } 
        return false;
    }


    public void discardBeanCard(BeanCard card) {
        if (hand.remove(card)) {
            System.out.println(name + " discarded: " + card);
        } else {
            System.out.println("Error: Card not found in hand to discard.");
        }
    }
    
    public void notifyTradeProposal(Trade trade) {
        Player otherPlayer = (trade.getPlayer1() == this) ? trade.getPlayer2() : trade.getPlayer1();
        List<BeanCard> offeredCards = trade.getOfferedCards(otherPlayer);
        List<BeanCard> myCards = trade.getOfferedCards(this);

        System.out.println(otherPlayer.getName() + " proposes a trade:");
        System.out.println("They offer: " + offeredCards);
        if (!myCards.isEmpty()) {
            System.out.println("You offered: " + myCards);
        }
        System.out.print("Do you accept? (y/n): ");
        String choice = game.getScanner().nextLine().toLowerCase();
        if (choice.equals("y")) {
            if (trade.acceptTrade(this)) { 
                System.out.println("Trade accepted!");
            } else {
                System.out.println("The other player already declined or the trade is invalid.");
            }
        } else {
            trade.declineTrade();
            System.out.println("Trade declined.");
        }
    }
    
    public void proposeTrade(Player otherPlayer, List<BeanCard> offeredCards) {
        Trade trade = new Trade(this, otherPlayer, game); // Create a new Trade object
        trade.proposeTrade(this, offeredCards);
    }
    
    public List<BeanCard> getCardsInHand() {
        return new ArrayList<>(hand); // Return a copy of the hand to prevent external modification
    }
}
