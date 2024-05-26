import java.util.*;
public class Game {
        // ...inside the Game class...
	private List<BeanCard> tradeOffer = new ArrayList<>();
	private List<Player> players;
	private Player currentPlayer;
    private Deck cards;
    private int round;
    private boolean gameOver;
    private final int MAX_TRADES_PER_TURN = 3;
    private final int MAX_ROUNDS = 3;
	Scanner scanner = new Scanner(System.in);
	
	 public Game(List<Player> players) {
		 this.players = players;
		 this.cards = new Deck();
		 this.round = 1;
		 this.gameOver = false;
		 this.tradeOffer = new ArrayList<>();
	 }
	 

    public void startGame() {
        cards.shuffleCards();

        for (Player player : players) {
            for (int i = 0; i < 3; i++) { // Initial 3 cards per player in Bohnanza
                player.drawCard(pickCardFromDeck());
            }
        }
        this.currentPlayer = players.get(0); 
    }
	  
    public BeanCard pickCardFromDeck() {
        return cards.drawCard(); // Assuming drawCard() handles empty deck scenarios
    }
	  
    private void takeTurn(Player player) {
        int numTrades = 0;

        // 1. Draw Cards
        BeanCard card1 = pickCardFromDeck();
        BeanCard card2 = pickCardFromDeck();
        player.drawCard(card1);
        player.drawCard(card2);
        System.out.println("You drew: " + card1 + " and " + card2);

        // 2. Plant First Card (Mandatory)
        plantCard(player, card1, true); 

        // 3. Plant Second Card (Optional)
        System.out.print("Do you want to plant the second card? (y/n): ");
        String plantChoice = scanner.nextLine().toLowerCase();
        if (plantChoice.equals("y")) {
            plantCard(player, card2, false);
        } else {
            player.discardBeanCard(card2); 
        }

        // 4. Trading Phase
        System.out.print("Do you want to trade? (y/n): ");
        String tradeChoice = scanner.nextLine().toLowerCase();
        while (tradeChoice.equals("y") && numTrades < MAX_TRADES_PER_TURN) {
            handleTrade(player, players, scanner);
            numTrades++;
            if (numTrades < MAX_TRADES_PER_TURN) {
                System.out.print("Do you want to trade again? (y/n): ");
                tradeChoice = scanner.nextLine().toLowerCase();
            }
        }

        // 5. Harvest Phase (Optional)
        System.out.print("Do you want to harvest any fields? (y/n): ");
        String harvestChoice = scanner.nextLine().toLowerCase();
        while (harvestChoice.equals("y")) {
            harvestField(player, scanner); 
            System.out.print("Do you want to harvest another field? (y/n): ");
            harvestChoice = scanner.nextLine().toLowerCase();
        }
    }
  
  private boolean checkEndGame() {
    // Check if all players have at least three empty fields
        int emptyFieldCount = 0;
        for(Player player : players){
            for(Field field : player.getFields()){
                if(field.getBeans().isEmpty()){
                    emptyFieldCount++;
                }
            }
            if(emptyFieldCount >= 3){
                gameOver = true;
                return true;
            }
            emptyFieldCount = 0;
        }
        //Check if max rounds limit has reached
        if(round > MAX_ROUNDS){
            gameOver = true;
            return true;
        }

        return false;
  }
  
  

  private void endGame() {
      gameOver = true;
      List<Player> sortedPlayers = getScore();

      System.out.println("\nGame Over!\nFinal Scores:");
      for (Player player : sortedPlayers) {
          System.out.println(player.getName() + ": " + player.getScore() + " gold coins");
      }

      Player winner = sortedPlayers.get(0);
      System.out.println(winner.getName() + " wins!");
      scanner.close(); // Close the scanner when the game ends
  }
	 
	    
    public void playRound() {
        int currentPlayerIndex = 0;
        while (!gameOver) {
            //Display the round number and the details of the current player.
            System.out.println("\n-- Round " + round + " --");
            Player player = players.get(currentPlayerIndex);
            currentPlayer = player;
            System.out.println("Player " + player.getName() + "'s turn:");
            takeTurn(player);

            // Check if the game should end
            if (checkEndGame()) {
                break;
            }
            
            // Move to the next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            round++;
        }
        endGame(); //End the game if end game condition is true.
    }

    
    public void plantCard(Player player, BeanCard card, boolean mandatory) {
        // ... (Logic for planting a card in a field, considering mandatory/optional planting) ...
        System.out.println("Available fields:");
        for (int i = 0; i < player.getFields().size(); i++) {
            Field field = player.getFields().get(i);
            System.out.println((i + 1) + ": " + field);
        }
    
        int fieldIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
    
        if (fieldIndex >= 0 && fieldIndex < player.getFields().size()) {
            Field selectedField = player.getFields().get(fieldIndex);
    
            if (mandatory || selectedField.canPlantBean(card.getType())) { // Check if planting is allowed
                player.plantBeanCard(selectedField, card);
                System.out.println("Planted " + card + " in field " + (fieldIndex + 1));
            } else {
                System.out.println("You cannot plant this bean type in this field.");
                if (!mandatory) {
                    player.discardBeanCard(card); // Discard if not mandatory
                }
            }
        } else {
            System.out.println("Invalid field choice.");
            // Handle error (ask for input again or force discard if mandatory)
        }
    }
    
    private void handleTrade(Player player, List<Player> players, Scanner scanner) {
        List<BeanCard> offeredCards = player.selectCardsForTrade(scanner);
        Player otherPlayer = player.selectTradingPartner(players, scanner);
        
        if (otherPlayer != null && !offeredCards.isEmpty()) {
        	Trade trade = new Trade(player, otherPlayer, this);
            tradeOffer = offeredCards; 
            player.proposeTrade(otherPlayer, offeredCards);
            
            if (otherPlayer.acceptTrade(trade)) {
                System.out.println("Trade successful!");
                tradeOffer.clear(); // Clear the trade offer after a successful trade
            } else {
                System.out.println("Trade declined.");
                player.getHand().addAll(offeredCards); // Return cards to player's hand if trade declined
            }
        }
    }
    
    private void harvestField(Player player, Scanner scanner) { 
        System.out.println("Available fields:");
        for (int i = 0; i < player.getFields().size(); i++) {
            Field field = player.getFields().get(i);
            System.out.println((i + 1) + ": " + field);
        }

        System.out.print("Choose a field to harvest (1-" + player.getFields().size() + "): ");
        int fieldIndex = scanner.nextInt() - 1; // Use the passed scanner for input
        scanner.nextLine(); // Consume newline

        if (fieldIndex >= 0 && fieldIndex < player.getFields().size()) {
            Field selectedField = player.getFields().get(fieldIndex);
            player.harvestBeans(selectedField); // Perform harvesting
        } else {
            System.out.println("Invalid field choice.");
        }
    }
    
    // ... (rest of the Game class as before)

    public List<Player> getScore() {
        List<Player> sortedPlayers = new ArrayList<>(players);
        
        // Calculate scores for each player
        for (Player player : players) {
            player.score = 0; // Reset score at the beginning of calculation
            for (Field field : player.getFields()) {
                Map<BeanCardType, Integer> beanCounts = new HashMap<>();
                for (BeanCard card : field.getBeans()) {
                    beanCounts.put(card.getType(), beanCounts.getOrDefault(card.getType(), 0) + card.getQuantity());
                }
                
                for (Map.Entry<BeanCardType, Integer> entry : beanCounts.entrySet()) {
                    BeanCardType type = entry.getKey();
                    int count = entry.getValue();
                    player.score += type.calculateScore(count); 
                }
            }
        }
        
        // Sort players based on their scores (highest first)
        Collections.sort(sortedPlayers, Comparator.comparingInt(Player::getScore).reversed());
        
        return sortedPlayers;
    }
    
    // ... (rest of the Game class as before) ...

    public Scanner getScanner() {
        return scanner;
    }
    
    public void onTradeCompleted() {
        // Update game state, UI, etc. as needed after a successful trade
        System.out.println("Trade completed successfully!");
    }

// ... (rest of the Game class remains the same) ...



}