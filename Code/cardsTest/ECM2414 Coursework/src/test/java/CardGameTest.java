import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class CardGameTest {

    String INPUT_PATH = "C:\\Users\\Ghali\\IdeaProjects\\";
    //TEST_OUTPUT_DIRECTORY_PATH
    private static final String TEST_OUTPUT_DIRECTORY_PATH = "output\\test\\";


    static{
        //setupFilePath
        File outputDirectory = new File(TEST_OUTPUT_DIRECTORY_PATH);
        if(!outputDirectory.exists()) {
            outputDirectory.mkdir();
            System.out.println("Created: " + TEST_OUTPUT_DIRECTORY_PATH);
        }

    }

    /**
     * Tests whether a non-numeric number of players is accepted.
     */
    @Test
    void numberOfPlayersIsNotNumeric() {
        var game = new CardGame();

        assertThrows(IllegalArgumentException.class,
                () -> {
                    game.setNumberOfPlayersFromString("abc");
                });
        game.resetIDs();
    }

    /**
     * Tests whether a non-valid number of players is accepted.
     */
    @Test
    void numberOfPlayersIsNotNonZeroPositiveInteger() {
        var game = new CardGame();
        assertThrows(IllegalArgumentException.class,
                () -> {
                    game.setNumberOfPlayersFromString("0");
                });
        game.resetIDs();
    }


    /**
     * Tests whether a valid pack, for the correct number of players can be loaded.
     */
    @Test
    void testLoadPackFromFileValidPath() {
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\four.txt";
        var game = new CardGame();
        game.setNumberOfPlayersFromString("4");
        assertDoesNotThrow(
                () -> {
                    game.loadPackFromFile(filePath);
                });
        game.resetIDs();
    }


    /**
     * Tests whether a valid pack, for the correct number of players can be loaded.
     */
    @Test
    void testLoadPackFromFileInvalidPath() {
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\fourXyz.txt";
        var game = new CardGame();
        game.setNumberOfPlayersFromString("4");
        assertThrows(IOException.class,
                () -> {
                    game.loadPackFromFile(filePath);
                });
        game.resetIDs();
    }

    /**
     * Tests whether a pack containing a string of letters or not.
     */
    @Test
    void testLoadPackFromFileWithAlphabetic() {
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\four-invalid-1.txt";
        var game = new CardGame();
        game.setNumberOfPlayersFromString("4");
        assertThrows(NumberFormatException.class,
                () -> {
                    game.loadPackFromFile(filePath);
                });
        game.resetIDs();
    }

    /**
     * Tests whether a pack containing an invalid number of cards for a game.
     */
    @Test
    void LoadPackFromFileWithMoreCardsTest() {
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\four-invalid-2.txt";
        var game = new CardGame();
        game.setNumberOfPlayersFromString("4");
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> {
                    game.loadPackFromFile(filePath);
                });
        game.resetIDs();
    }

    /**
     * Tests whether a pack containing an invalid number of cards for a game.
     */
    @Test
    void LoadPackFromFileWithNegativeTest() {
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\four-invalid-3.txt";
        var game = new CardGame();
        game.setNumberOfPlayersFromString("4");
        assertThrows(NumberFormatException.class,
                () -> {
                    game.loadPackFromFile(filePath);
                });
        game.resetIDs();
    }


    /**
     * Tests whether a valid pack, for the correct number of players can be loaded.
     */
    @Test
    void dealCardsForPlayersTest() {
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\eight.txt";
        var game = new CardGame();
        int playersCount = 8;
        game.setNumberOfPlayersFromString("" + playersCount);
        assertDoesNotThrow(
                () -> {
                    game.loadPackFromFile(filePath);
                });

        ArrayList<Player> players = game.getPlayers();
        LinkedList<Card> cards = game.getCards();
        int totalCardsCount = cards.size();

        game.dealCardsForPlayers();

        int index = 0;
        //check for the value of the top card in each player's hand
        assertEquals(65, players.get(index++).getHand().peek());
        assertEquals(2, players.get(index++).getHand().peek());
        assertEquals(3, players.get(index++).getHand().peek());
        assertEquals(4, players.get(index++).getHand().peek());
        assertEquals(1, players.get(index++).getHand().peek());
        assertEquals(2, players.get(index++).getHand().peek());
        assertEquals(24, players.get(index++).getHand().peek());
        assertEquals(40, players.get(index++).getHand().peek());

        //check for the value of the top card in cards
        assertEquals(1, cards.peek().getValue());

        //check for remaining cards count
        assertEquals(totalCardsCount - playersCount * 4, cards.size());
    }


    /**
     * Tests whether a valid pack, for the correct number of players can be loaded.
     */
    @Test
    void DealCardsForDecksTest() {
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\eight.txt";
        var game = new CardGame();
        int playersCount = 8;
        game.setNumberOfPlayersFromString("" + playersCount);
        assertDoesNotThrow(
                () -> {
                    game.loadPackFromFile(filePath);
                });

        LinkedList<Card> cards = game.getCards();
        ArrayList<CardDeck> decks = game.getDecks();
        int totalCardsCount = cards.size();

        game.dealCardsForPlayers();

        //check for remaining cards count
        assertEquals(totalCardsCount - playersCount * 4, cards.size());

        game.dealCardsForDecks();

        int index = 0;
        //check for the value of the top card in each deck
        assertEquals(1, decks.get(index++).getCards().peek());
        assertEquals(2, decks.get(index++).getCards().peek());
        assertEquals(3, decks.get(index++).getCards().peek());
        assertEquals(4, decks.get(index++).getCards().peek());
        assertEquals(1, decks.get(index++).getCards().peek());
        assertEquals(2, decks.get(index++).getCards().peek());
        assertEquals(24, decks.get(index++).getCards().peek());
        assertEquals(40, decks.get(index++).getCards().peek());

        //check for remaining cards count
        assertEquals(0, cards.size());
    }


    /**
     * Tests whether the initial hand for player 1 is writing correctly by comparing it to a pre-write sentence .
     */
    @Test
    void testWriteInitialHandOfPlayersToFile() {
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\eight.txt";
        var game = new CardGame();
        int playersCount = 8;
        game.setNumberOfPlayersFromString("" + playersCount);
        assertDoesNotThrow(
                () -> {
                    game.loadPackFromFile(filePath);
                });

        //String testOutputDirectory = GLOBALPATH + "ECM2414 Coursework\\doc\\TEST_OUTPUT\\";

        game.dealCardsForPlayers();
        game.writeInitialHandOfPlayersToFile(TEST_OUTPUT_DIRECTORY_PATH);

        String outputFilePath = TEST_OUTPUT_DIRECTORY_PATH + "player1_output.txt";
        String expected = "Player 1 initial hand is 65 1 1 1";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(outputFilePath));
            String line = reader.readLine();
            assertEquals(expected, line);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }
    /**
     * Tests the exception handling of writing a players hand to a player output file.
     */
    @Test
    void writeInitialHandOfPlayersInFileExceptionTest(){
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\eight.txt";
        var game = new CardGame();
        int playersCount = 8;
        game.setNumberOfPlayersFromString("" + playersCount);
        assertDoesNotThrow(
                () -> {
                    game.loadPackFromFile(filePath);
                });


        game.dealCardsForPlayers();

        assertThrows(RuntimeException.class,
                () -> {
                    game.writeInitialHandOfPlayersToFile("WRONG//FILE//PATH");
                });

    }

    /**
     * Tests whether a winner of the game is found when one or more players currently have a winning hand.
     */
    @Test
    void checkWinnersTest(){
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\four.txt";
        var game = new CardGame();
        int playersCount = 4;
        game.setNumberOfPlayersFromString("" + playersCount);
        assertDoesNotThrow(
                () -> {
                    game.loadPackFromFile(filePath);
                });

        game.dealCards();
        game.checkWinners();
        assertEquals(1,game.winner);
    }

    /**
     * Tests whether a message can be outputted to a player output file.
     */
    @Test
    void outputPlayerMsgWriteToFileTest(){}

    /**
     * Tests the exception handling of writing messages to a player output file.
     */
    @Test
    void outputPlayerMsgWriteToFileExceptionTest(){
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\four.txt";
        var game = new CardGame();
        int playersCount = 4;
        game.setNumberOfPlayersFromString("" + playersCount);
//        String testOutputDirectory = GLOBALPATH + "ECM2414 Coursework\\doc\\TEST_OUTPUT\\";


        assertDoesNotThrow(
                () -> {
                    game.loadPackFromFile(filePath);
                });
        game.dealCards();
        game.writeInitialHandOfPlayersToFile(TEST_OUTPUT_DIRECTORY_PATH);

        assertThrows(RuntimeException.class,
                () -> {
                    game.outputPlayerMsg("Hello World", 0, "ECM2414 Coursework\\d\\output\\");
                });
    }

    /**
     * Tests the exception handling of writing messages to a deck output file.
     */
    @Test
    void outputDeckExceptionTest(){
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\eight.txt";
        var game = new CardGame();
        int playersCount = 8;
        game.setNumberOfPlayersFromString("" + playersCount);
        assertDoesNotThrow(
                () -> {
                    game.loadPackFromFile(filePath);
                });


//        String testOutputDirectory = GLOBALPATH + "ECM2414 Coursework\\d\\TEST_OUTPUT\\"; //Incorrect Path

        game.dealCardsForPlayers();

        assertThrows(RuntimeException.class,
                () -> {
                    game.outputDeck("wrong//test//path//");
                });

    }

    /**
     * Tests whether the games objects can be setup correctly and the pack loaded.
     */
    @Test
    void setupTest(){

        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\four.txt";
        var game = new CardGame();
        game.setup("4", filePath, true);


        assertEquals(game.getPlayers().size(), 4);


    }

    /**
     * Test whether the expected card is drawn from the expected deck.
     */
    @Test
    void drawCardTest(){
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\eight.txt";
        var game = new CardGame();
        String playersCount = "8";
        game.setup(playersCount, filePath, true);
        game.dealCards();

        game.drawCard(0);
        assertEquals(game.getPlayers().get(0).getHand().size(), 5);
    }

    /**
     * Tests whether a card is discarded to the correct deck, and the card discarded follows the gameplay strategy.
     */
    @Test
    void discardCardTest(){
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\eight.txt";
        var game = new CardGame();
        String playersCount = "8";
        game.setup(playersCount, filePath, true);
        game.dealCards();

        game.drawCard(0);
        game.discardCard(0);
        assertEquals(game.getPlayers().get(0).getHand().size(), 4);}

    /**
     * Tests that the gameplay action for one player runs correctly.
     */
    @Test
    void gameplayTest(){
        String filePath = INPUT_PATH + "ECM2414 Coursework\\doc\\packs\\test_packs\\five.txt";
        var game = new CardGame();

        String playersCount = "5";
        game.setup(playersCount, filePath, true);
        game.dealCards();

        game.gameplay(0);
        assertEquals(game.winner, 0);
    }
}
