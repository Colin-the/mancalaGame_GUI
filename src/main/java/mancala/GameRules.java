package mancala;
import java.io.Serializable;
/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable {
    private final MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)
    private static final long serialVersionUID = 4L;

    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        gameBoard = new MancalaDataStructure();
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    public MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    /* default */ boolean isSideEmpty(final int pitNum) {
        boolean sideEmpty=true;
        if (pitNum < 1 || pitNum > 12) {
            throw new PitNotFoundException("Invalid pit number. Pit number must be in the range of 1-12.");
        }else if (pitNum <=6){//first 6 pits
            for (int i = 1; i <= 6; i++) {
                if (getNumStones(i) != 0) {
                    sideEmpty=false; // at least one pit has stones
                    break;//no need to check further
                }
            }
        }else {//last 6 pits
            for (int i = 7; i <= 12; i++) {
                if (getNumStones(i) != 0) {
                    sideEmpty=false; // at least one pit has stones
                    break;//no need to check further
                }
            }
        }
        return sideEmpty;
    }
    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    public void setPlayer(final int playerNum) {
        this.currentPlayer = playerNum;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum);

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * Note: This method intentionally uses the default package-private access modifier.
     * 
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    /* default */ abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * Note: This method intentionally uses the default package-private access modifier.
     * 
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    /* default */ abstract int captureStones(int stoppingPoint);

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(final Player one, final Player two) {
        // this method can be implemented in the abstract class.
        currentPlayer = 1;
        final Store store1 = new Store();
        final Store store2 = new Store();
        gameBoard.setStore(store1,1);
        gameBoard.setStore(store2,2);
        store1.setOwner(one);
        store2.setOwner(two);
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    @Override
    public String toString() {
        // Implement toString() method logic here.

        return "";
    }
}
