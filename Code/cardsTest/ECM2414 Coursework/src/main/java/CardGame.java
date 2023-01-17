import java.io.*;
import java.util.*;

public class CardGame extends Thread{
    private static final String OUTPUT_DIRECTORY_PATH = "output\\";
    private int numberOfPlayers;
    int winner = -1; // -1 when there is no winner
    private final LinkedList<Card> cards;
    private volatile Boolean isOver = false;
    private final ArrayList<Player> players;
    private final ArrayList<CardDeck> decks;
    public final String GLOBALPATH = ".\\";
    private volatile LinkedList<Integer> allPlayers = new LinkedList<>();

    /**
     * Creates an instance of the CardGame.
     */
    public CardGame(){
        this.cards = new LinkedList<>();
        this.players = new ArrayList<>();
        this.decks = new ArrayList<>();
        resetIDs();
    }

    /**
     * A method to return all the cards in the pack (all the cards used in the game).
     *
     * @return A LinkedList of all objects of type Card
     */
    public LinkedList<Card> getCards() {
        return cards;
    }

    /**
     * A method to get all the players in the game.
     *
     * @return An ArrayList of all objects of type Player
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * A method to get all the decks in the game.
     *
     * @return An ArrayList of all objects of type CardDeck
     */
    public ArrayList<CardDeck> getDecks() {
        return decks;
    }


    /**
     * Resets all IDs, to prevent incorrect IDs being given when creating multiple instance of the game.
     */
    public void resetIDs(){
        Player.resetPlayerID();
        CardDeck.resetDeckID();
    }

    /**
     * Transforms the user input from a string into an integer
     *
     * @param stringNumberOfPlayers The string inputted to be turned into an integer
     * @throws IllegalArgumentException Thrown when what is inputted is not valid
     */
    public void setNumberOfPlayersFromString(String stringNumberOfPlayers) throws IllegalArgumentException{
        numberOfPlayers = Integer.parseInt(stringNumberOfPlayers);
        if (numberOfPlayers <= 0) {
            throw new IllegalArgumentException();
        }

        //ADDS PLAYERS
        for (int i = 0; i < numberOfPlayers; i++) {
            allPlayers.add(i);
        }
        //SHUFFLES PLAYERS
        Collections.shuffle(allPlayers);


        for (int i=0; i<numberOfPlayers; i++) {
            players.add(new Player(numberOfPlayers, "Player " + (i+1)));
            decks.add(new CardDeck("Deck" + i));
        }
    }

    /**
     * Checks whether the pack which has been inputted is a valid pack following the rules of the game
     *
     * @param filePath The location of the file containing the pack, as a txt.
     * @return True if the pack is valid following the rules of the game.
     * @throws IOException thrown when the filepath is invalid.
     * @throws IndexOutOfBoundsException if the file contains more or less than the required number of cards.
     * @throws NumberFormatException when the file contains an illegal card value.
     */
    public boolean loadPackFromFile(String filePath)
            throws NumberFormatException, ArrayIndexOutOfBoundsException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String stringCard;
        while ((stringCard = reader.readLine())!= null){
            int cardValue = Integer.parseInt(stringCard);
            if (cardValue < 0){
                throw new NumberFormatException(); // Thrown when an illegal card value is given
            }
            cards.add(new Card(0, 0, cardValue));
        }

        int numberOfCards = 8 * numberOfPlayers;
        if (cards.size() == numberOfCards ){
            return true;
        }
        else {
            throw new ArrayIndexOutOfBoundsException(); // Thrown when there are too many or too little cards
        }
    }

    /**
     * Sets up the game, by establishing the number of players in the game,
     * and loads the pack containing all the cards used in the game, checking if they match with the rules.
     * Doesn't exit until appropriate number of players and files have been inputted.
     *
     * @param stringNumberOfPlayers The number of players playing the game that the user inputted
     * @param packFileName The location of the pack file.
     */
    public void setup(String stringNumberOfPlayers, String packFileName, Boolean testing) {

        File outputDirectory = new File(OUTPUT_DIRECTORY_PATH);
        if(outputDirectory.exists()) {
            Arrays.stream(Objects.requireNonNull(outputDirectory).listFiles()).forEach(File::delete);
        }
        else{
            outputDirectory.mkdir();
        }
        Scanner scanner = new Scanner(System.in);

        boolean validNumberOfPlayers = false;
        if(!testing) {
            do {
                System.out.println("Please enter the number of players:");
                stringNumberOfPlayers = scanner.nextLine();

                try {
                    setNumberOfPlayersFromString(stringNumberOfPlayers);
                    validNumberOfPlayers = true;

                } catch (IllegalArgumentException e) { // Thrown when user inputs non-zero negative integer
                    System.out.println("Invalid input. Please enter a non zero positive number of players.");
                }
            } while (!validNumberOfPlayers);
        } else {
            setNumberOfPlayersFromString(stringNumberOfPlayers);
        }

        boolean validPack = false;
        if(!testing) {
            do {
                System.out.println("Please enter the location of pack to load:");
                packFileName = scanner.nextLine();
                try {
                    validPack = loadPackFromFile(packFileName);
                } catch (NumberFormatException e) { // Thrown when the pack contains non-integer values
                    System.out.println("File contains invalid Card values.");
                } catch (ArrayIndexOutOfBoundsException e) { // Thrown when there are too many values
                    System.out.println("File has more cards than required for players count: " + numberOfPlayers);
                } catch (IOException e) { // Thrown when the pack cannot be read
                    System.out.println("Could not read from file: " + packFileName);
                }
            } while (!validPack);
        } else{
            try {
                loadPackFromFile(packFileName);
            }catch(IOException e){
                System.out.println("Test filename does not exist7");
            }
        }
    }


    /**
     * Deals 4 cards to all the players in the game from the pack, in a round-robin fashion starting from player 1.
     */
    public void dealCardsForPlayers() {
        //deal cards for all 'players'
        for(int i=0; i<4; i++) {
            for (int j = 0; j < numberOfPlayers; j++) {
                Card topCard = cards.pop();
                players.get(j).addCard(topCard.getValue());
            }
        }
    }

    /**
     * Deals 4 cards to all the decks in the game from the remaining cards in the pack, again in a round-robin fashion,
     * starting from deck 1
     */
    public void dealCardsForDecks() {
        //deal cards for all 'decks'
        for(int i=0; i<4; i++) {
            for (int j = 0; j < numberOfPlayers; j++) {
                Card topCard = cards.pop();
                decks.get(j).addCard(topCard.getValue());
            }
        }
    }

    /**
     * Deals 4 cards to all players in a round-robin fashion, and then deals the remaining cards to the decks.
     */
    public void dealCards(){
        dealCardsForPlayers();
        dealCardsForDecks();
        writeInitialHandOfPlayersToFile((OUTPUT_DIRECTORY_PATH));
    }

    /**
     * Outputs a message to a players output file.
     *
     * @param sentence The message to be outputted to the file
     * @param player The player whose file is being outputted to.
     * @param path the path to the output files
     */
    public void outputPlayerMsg(String sentence, int player, String path){
        try {
            FileWriter pWrite = new FileWriter(path + File.separator + "player" + (player + 1) + "_output.txt", true);
            pWrite.write(sentence);
            pWrite.close();
        } catch (IOException e) { // caught if the path does not exist.
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes to the corresponding player output files, the cards initially in each of the players' hands.
     *
     * @param directory The location of the directory where the output files are to be written to
     */
    public void writeInitialHandOfPlayersToFile(String directory) {
        for(int i=1; i<=numberOfPlayers; i++) {
            try {
                BufferedWriter pWrite = new BufferedWriter(new FileWriter(directory + "player" + i + "_output.txt"));
                pWrite.write(players.get(i-1).getPlayerName() + " initial hand is " + players.get(i-1).getHandAsString());
                pWrite.close();
            } catch (IOException e) { // Caught if the file path is not correct
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Checks if any player currently has a winning hand, if so the global variable isOver will be set to true.
     */
    public synchronized void checkWinners(){
        for(int i=0; i < numberOfPlayers; i++) {
            if (players.get(i).checkWin()){
                winner = players.get(i).getPlayerID();
            }
        } // Finds the winner

        writeWinnerInFile(); // Writes the winner to all the player output files
    }

    /**
     * Writes to each of the player files, who has won, as well as each of the players final hand.
     */
    private void writeWinnerInFile() {
        if(winner != - 1) {
            System.out.println("Player " + (winner + 1) + " has won");
            isOver = true;
            for(int i=0; i < numberOfPlayers; i++){
                if(winner == players.get(i).getPlayerID()){
                    outputPlayerMsg("\nPlayer " + (i + 1) + " wins", i, OUTPUT_DIRECTORY_PATH);
                } else{
                    outputPlayerMsg("\nPlayer " + (winner + 1) + " has informed player " + (i+1) + " that player " + (winner + 1) + " has won", i, OUTPUT_DIRECTORY_PATH);
                }
                outputPlayerMsg("\nPlayer " + (i + 1) + " exits", i, OUTPUT_DIRECTORY_PATH);
                outputPlayerMsg("\nPlayer " + (i + 1) + " final hand: " + players.get(i).getHandAsString(), i, OUTPUT_DIRECTORY_PATH);
                outputDeck(OUTPUT_DIRECTORY_PATH);
            }
            interrupt(); // Prevents other threads from continuing the game process
        }
    }

    /**
     * Outputs the contents of all decks in the game to the appropriate deck output files.
     *
     * @param path The location of the pack, inputted by the user.
     */
    public void outputDeck(String path){
        for(int i=1; i<=numberOfPlayers; i++) {
            try {
                BufferedWriter dWrite = new BufferedWriter(new FileWriter(path + File.separator + "deck" + i + "_output.txt"));
                dWrite.write("Deck " + i + " contents: " + decks.get(i - 1).getCards());
                dWrite.close();
            } catch (IOException e) { // Caught if the file path is not correct
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Draws a card for a given player in the game, then write it to player output file
     *
     * @param playerIndex The index of the player who will be drawing a card (zero-indexed)
     */
    public synchronized void drawCard(int playerIndex) {
        players.get(playerIndex).addCard(decks.get(playerIndex).drawCard());
        outputPlayerMsg("\nPlayer " + (playerIndex + 1) + " draws a " + players.get(playerIndex).getHand().get(4) + " from deck " + (playerIndex + 1), playerIndex, OUTPUT_DIRECTORY_PATH);
    }

    /**
     * Discards a card from a players hand to the appropriate deck, then write it to the corresponding deck output
     *
     * @param playerIndex The index of the player who will be drawing a card (zero-indexed)
     */
    public synchronized void discardCard(int playerIndex){
        int value = players.get(playerIndex).removeCard();
        int discardDeckIndex = players.get(playerIndex).getDiscardDeckID();
        decks.get(players.get(playerIndex).getDiscardDeckID()).addCard(value);
        outputPlayerMsg("\nPlayer " + (playerIndex + 1) + " discards a " + value + " to deck " + (discardDeckIndex + 1), playerIndex, OUTPUT_DIRECTORY_PATH);
        outputPlayerMsg("\nPlayer " + (playerIndex + 1) + "'s current hand is " + players.get(playerIndex).getHandAsString(), playerIndex, OUTPUT_DIRECTORY_PATH);
    }

    /**
     * Draws a card for a given player in the game, then calls for the player to discard a card, and checks if there are any winners.
     *
     * @param playerIndex The index of the player who will be drawing a card (zero-indexed)
     */
    public synchronized void gameplay(int playerIndex){ // synchronized to assure thread safety
        if(!isOver && !decks.get(playerIndex).getCards().isEmpty()) {
            notifyAll();
            drawCard(playerIndex);
            discardCard(playerIndex);
            checkWinners();
            return;
        }
        try {
            wait();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        // Assures that player 1 does not always go first
        int player = allPlayers.pop();

        while (!isOver) {
            gameplay(player);

        }
    }

    public static void main(String[] args)  {
        String stringNumberOfPlayers = "";
        String packFileName = "";

        CardGame play = new CardGame();
        play.setup(stringNumberOfPlayers, packFileName, false);
        play.dealCards();
        play.checkWinners();

        if(!play.isOver){ // If no one wins the game with their initial hand, the game begins
            for (int i = 1; i <= play.numberOfPlayers; i++) {
                Thread t = new Thread(play, "thread" + i);
                t.start();
            }
        }
    }


}



