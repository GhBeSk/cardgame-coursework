import java.util.LinkedList;

public class CardDeck {
    private static int nextAvailableID = 0;
    private final int deckID;
    private volatile int numberOfCardsInDeck;
    private volatile LinkedList<Integer> cards;
    private final String name;

    /**
     * Constructs an instance of CardDeck.
     *
     * @param name The name of the deck, used to create a deck output file
     */
    public CardDeck(String name){
        this.deckID = nextAvailableID;
        incrementDeckID();
        numberOfCardsInDeck = 0;
        this.name = name;
        this.cards = new LinkedList<>();

    }

    /**
     * Gets the cards in a deck.
     *
     * @return A LinkedList of the values of the cards in the deck
     */
    public LinkedList<Integer> getCards() {return this.cards;}

    /**
     * Adds a card to a deck, from the pack.
     *
     * @param newCard The Value of the card to be added to a deck
     */
    public void addCard(int newCard){
        cards.add(cards.size(), newCard);
    }

    /**
     * Draws a card from the deck.
     *
     * @return The card which has been drawn
     */
    public int drawCard(){
        return cards.pop();
    }

    /**
     * Increments the next available id of the deck.
     */
    private static void incrementDeckID(){
        nextAvailableID++;
    }

    /**
     * Resets the Deck IDs in case of a new game being made
     */
    public static void resetDeckID(){
        nextAvailableID = 0;
    }
}
