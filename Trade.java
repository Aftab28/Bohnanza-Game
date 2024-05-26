import java.util.ArrayList;
import java.util.List;

public class Trade {
    private Player player1;
    private Player player2;
    private List<BeanCard> offeredCards1;
    private List<BeanCard> offeredCards2;
//    private Game game;
    private TradeStatus status; // Added to track the status of the trade

    public enum TradeStatus {
        PROPOSED,
        ACCEPTED,
        DECLINED
    }

    public Trade(Player player1, Player player2, Game game) {
        this.player1 = player1;
        this.player2 = player2;
//        this.game = game; // Initialize the game reference
        this.offeredCards1 = new ArrayList<>();
        this.offeredCards2 = new ArrayList<>();
        this.status = TradeStatus.PROPOSED; 
    }

    // Getters
    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public List<BeanCard> getOfferedCards(Player player) {
        if (player == player1) {
            return offeredCards1;
        } else if (player == player2) {
            return offeredCards2;
        } else {
            throw new IllegalArgumentException("Player not involved in this trade.");
        }
    }

    public TradeStatus getStatus() {
        return status;
    }

    // Actions
    public void proposeTrade(Player offeringPlayer, List<BeanCard> cards) {
        if (offeringPlayer != player1 && offeringPlayer != player2) {
            throw new IllegalArgumentException("Player not involved in this trade.");
        }

        List<BeanCard> offeredCards = (offeringPlayer == player1) ? offeredCards1 : offeredCards2;

        // Check if cards are in the player's hand and if the player has enough gold to buy the offer
        if (offeringPlayer.getCardsInHand().containsAll(cards)) { 
            offeredCards.addAll(cards);
            status = TradeStatus.PROPOSED; 
        } else {
            // Handle error: invalid offer (not enough cards or gold)
            System.out.println("Error: Invalid trade offer.");
        }
        
        if (cards.isEmpty() || !offeringPlayer.getHand().containsAll(cards)) {
            System.out.println("Error: Invalid trade offer. You don't have all the offered cards.");
            return;
        }

        offeredCards.addAll(cards);
        status = TradeStatus.PROPOSED;

        // Notify the other player about the trade proposal
        Player otherPlayer = (offeringPlayer == player1) ? player2 : player1;
        otherPlayer.notifyTradeProposal(this);
    }

    public boolean acceptTrade(Player acceptingPlayer) {
        if (acceptingPlayer != player1 && acceptingPlayer != player2) {
            throw new IllegalArgumentException("Player not involved in this trade.");
        }

        if (status != TradeStatus.PROPOSED) {
            return false; // Trade cannot be accepted if not in PROPOSED state
        }
        
        // If both players accepted the trade, then perform the trade exchange
        if (acceptingPlayer == player1 && player2.acceptTrade(this)) {
            status = TradeStatus.ACCEPTED;
            executeTrade(); // call the method to exchange the cards between players
            return true;
        } else if (acceptingPlayer == player2 && player1.acceptTrade(this)) {
            status = TradeStatus.ACCEPTED;
            executeTrade(); // call the method to exchange the cards between players
            return true;
        } else {
            return false;
        }
    }

    private void executeTrade() {
        // Exchange the cards between the players
        player1.getCardsInHand().removeAll(offeredCards1);
        player2.getCardsInHand().removeAll(offeredCards2);
        player1.getCardsInHand().addAll(offeredCards2);
        player2.getCardsInHand().addAll(offeredCards1);
        
        // Clear the offered cards lists
        offeredCards1.clear();
        offeredCards2.clear();
    }

    public void declineTrade() {
        status = TradeStatus.DECLINED;
        // Optionally, notify the other player that the trade was declined
    }

}
