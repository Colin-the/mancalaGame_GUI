package mancala;

import java.util.ArrayList;
import java.io.Serializable;

public class MancalaGame implements Serializable { 
    private static final long serialVersionUID = 14L;
    private Player onePlayer;
    private Player twoPlayer;
    private Player currentPlayer;
    private GameRules game;

    // Constructor
    public MancalaGame() {
        // Initialize the game with default values
        onePlayer = new Player("Player 1");
        twoPlayer = new Player("Player 2");
        setCurrentPlayer(onePlayer);
    }

    public void setPlayers(final Player playerOne, final Player playerTwo) {
        this.onePlayer = playerOne;
        this.twoPlayer = playerTwo;
        setCurrentPlayer(onePlayer);
        if (game!=null){
            game.registerPlayers(onePlayer,twoPlayer);
        }
    }

    public ArrayList<Player> getPlayers() {//ai writen method
        final ArrayList<Player> players = new ArrayList<>();
        players.add(onePlayer);
        players.add(twoPlayer);
        return players;
    }

    public Player getCurrentPlayer() {//ai writen method
        return currentPlayer;
    }

    public void setCurrentPlayer(final Player player) {
        this.currentPlayer = player;
    }

    public int getCurrentPlayerNumber(){
        int playerNu=0;
        if (currentPlayer.equals(onePlayer)){
            playerNu = 1;
        }else if (currentPlayer.equals(twoPlayer)){
            playerNu = 2;
        }
        return playerNu;
    }

    public void setBoard(final GameRules game) {
        this.game = game;
    }

    public GameRules getBoard() {//ai writen method
        return game;
    }

    public int getNumStones(final int pitNum) {
        if (pitNum < 1 || pitNum > 12) {//throw a expection if pitNum is not 1-12
            throw new PitNotFoundException("Invalid pit number");
        }
        return game.getNumStones(pitNum);
    }

    public int move(final int startPit) {
        //this will move the peices around the board to the correct pits
        int playerNumber=1;
        if (currentPlayer.equals(twoPlayer) && twoPlayer!=null){
            playerNumber=2;
        }      
        return game.moveStones(startPit,playerNumber); 
    }
    
    public int getStoreCount(final Player player) {
        int storeCount;
        if (player.equals(onePlayer)){
            storeCount= game.getDataStructure().getStoreCount(1);
        }else if (player.equals(twoPlayer)){
            //get the list of stores for the second player and then use the getTotalStones to return stoneCount
            storeCount= game.getDataStructure().getStoreCount(2);
        }else{
            //if no store is found throw expection
            throw new NoSuchPlayerException("Players store not found");
        } 
        return storeCount;
    }

    public Player getWinner(){
        if (!isGameOver()) {//if the game has not ended
            throw new GameNotOverException("Game is not over yet");
        }
    
        //get the total num of stones in both stores
        int numExtraSP1=0;
        for (int i = 0; i <6; i++){
            numExtraSP1+=game.getDataStructure().getNumStones(i+1);
        }
        game.getDataStructure().addToStore(1,numExtraSP1);

        int numExtraSP2=0;
        for (int i = 6; i <12; i++){
            numExtraSP2+=game.getDataStructure().getNumStones(i+1);
        }
        game.getDataStructure().addToStore(2,numExtraSP2);


        final int p1StonesInS = getStoreCount(onePlayer);
        final int p2StonesInS = getStoreCount(twoPlayer);
    
        //if the first player ended up with more stones in there store then they win
        if (p1StonesInS > p2StonesInS) {
            return onePlayer;
        } else if (p1StonesInS < p2StonesInS) {//if the 2nd player ended with more stones they win
            return twoPlayer;
        } else {//if they ended up with the same number of stones then its a tie
            return null; //null means we have a tie
        }
    }

    public boolean isGameOver() {
        return game.isSideEmpty(1) || game.isSideEmpty(7);
    }

    public void startNewGame() {
        if (onePlayer == null || twoPlayer == null) {// Players are not set, initialize with default values
            onePlayer = new Player("Player 1");
            twoPlayer = new Player("Player 2");
        }
        setCurrentPlayer(onePlayer); // Set the default starting player

        
    }    
    //ai writen method
    @Override
    public String toString() {
        // Implement a string representation of the game state
        return "MancalaGame state"; // Placeholder, replace with actual implementation
    }
}
