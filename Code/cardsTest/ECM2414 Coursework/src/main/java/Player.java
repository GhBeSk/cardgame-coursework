import java.util.LinkedList;
import java.util.Random;

public class Player {
    private static int nextAvailableID = 0;
    private final int playerID;
    private final int discardDeckID;
    private volatile LinkedList<Integer> hand = new LinkedList<>();
    private final String playerName;

    /**
     * Constructs an instance of the player class.
     *
     * @param numberOfPlayers The number of players in the game
     * @param name The name given to a player
     */
    public Player (int numberOfPlayers, String name){
        this.playerID = nextAvailableID;
        incrementPlayerID();
        if (numberOfPlayers > this.playerID + 1){
            this.discardDeckID = playerID + 1;
        }
        else{
            this.discardDeckID = 0;
        }
        this.playerName = name;

    }

    /**
     *  Gets the ID of a player.
     *
     * @return The player ID
     */
    public int getPlayerID(){
        return this.playerID;
    }

    /**
     * Gets the ID of the deck which the player discards their cards to.
     *
     * @return The deck ID
     */
    public int getDiscardDeckID(){
        return this.discardDeckID;
    }

    /**
     * Gets the all the cards in a players hand.
     *
     * @return A LinkedList containing all cards in the players hand
     */
    public LinkedList<Integer> getHand(){
        return this.hand;
    }

    /**
     * Gets the hand of a player in a format used in output files.
     *
     * @return A string of the values of all cards in a players hand separated by whitespace
     */
    public String getHandAsString(){
        return String.format("%s %s %s %s", hand.get(0), hand.get(1), hand.get(2), hand.get(3));
    }

    /**
     * Gets the name of the player.
     *
     * @return The player name
     */
    public String getPlayerName(){
        return this.playerName;
    }

    /**
     * Adds a card to this player's hand.
     *
     * @param cardValue The vale of the card to be added to the hand
     */
    public void addCard(int cardValue){
        hand.add(cardValue);
    }

    /**
     * Removes a card from the players hand.
     *
     * @return The value of the card that was removed.
     */
    public int removeCard(){
        int handSize = hand.size();
        while(true){
            Random randomNumber = new Random();
            int randomInt = randomNumber.nextInt(handSize);
            if(hand.get(randomInt) != (this.getPlayerID() + 1)){
                int removedCard = hand.get(randomInt);
                hand.remove(randomInt);
                return removedCard;
            }
        }
    }

    /**
     * Checks whether the player has a winning hand.
     *
     * @return True if the player has won the game.
     */
    public Boolean checkWin(){
        if(hand.size() == 4){
            return hand.get(0).equals(hand.get(1)) && hand.get(0).equals(hand.get(2)) && hand.get(0).equals(hand.get(3));
        }
        return false;
    }

    /**
     * Increments the next available id, for the next player.
     */
    private static void incrementPlayerID(){nextAvailableID++;}

    /**
     * Resets the player IDs in the case of a new game being created.
     */
    public static void resetPlayerID(){
        nextAvailableID = 0;
    }

}
