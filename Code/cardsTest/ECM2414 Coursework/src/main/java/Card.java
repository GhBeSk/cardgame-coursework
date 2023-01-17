public class Card {
    private static int numberOfCards = 1;
    private final int cardID;
    private int deckID;
    private int playerID;
    private final int value;

    /**
     * Constructs an instance of the Card class.
     *
     * @param deckID The id of the deck that a card belongs to, -1 if it isn't in a deck
     * @param playerID The id of the player whose hand the card is in, -1 if it isn't in a hand
     * @param value The value of the card
     */
    public Card (int deckID,int playerID, int value){
        this.cardID = numberOfCards;
        incrementCardID();
        this.deckID = deckID;
        this.playerID = playerID;
        this.value = value;
    }

    /**
     * Gets the value of the card
     *
     * @return The value of the card
     */
    public int getValue(){
        return this.value;
    }

    /**
     * Increments the next available id, for the next card.
     */
    private static void incrementCardID(){
        numberOfCards++;
    }
}
