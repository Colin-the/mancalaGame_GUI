package mancala;

public class AyoRules extends GameRules{
    private int currentPlayer=1;//as player 1 will make the fist move
    private int numAddedToStore=0;
    private static final long serialVersionUID = 1L;

    public AyoRules() {//default constructor
        super();
    }
    private void setCurrentPlayer(final int playerNumber){
        currentPlayer=playerNumber;
    }
    private int getCurrentPlayer(){
        return currentPlayer;
    }

    @Override
    public int moveStones(final int startPit, final int playerNum) {

        if (startPit > 12 || startPit < 1){//if the pit number does not exsist (not betewen 1 and 12)
            throw new InvalidMoveException("Invalid move. This pit does not exist!");
        } else if (playerNum==1 && startPit > 6 || playerNum==2 && startPit < 7){
            //if the player is trying to move stones they are not allowed to (player 1 tring to move player 2's stones)
            throw new InvalidMoveException("Invalid move. You are not allowed to move stones in the given pit.");
        }
        getDataStructure().setIterator(startPit,playerNum,true);
        setCurrentPlayer(playerNum);
        int currentPit = startPit, stonesToStore=0;
        boolean isStore=false;
        do{
            int stonesInPit = getNumStones(currentPit);
            
            int stonesDisreibuted = stonesInPit;
            //int beforeDistrbutePos = getDataStructure().currentIteratorPostion();

            distributeStones(currentPit);//distrbute the stones accros the board
             
            //IteratorPostion=0-13
            currentPit=getDataStructure().currentIteratorPostion();

            if (currentPit==6 ||currentPit==13){//if current is a store we will end the turn
                isStore=true;
            }else if (currentPit<6){
                currentPit++;//to convert 0-5 to 1-6 for pit numbers
            }

            

        }while (getNumStones(currentPit)>1 && !isStore);

        //only want to call captureStones IF we have moved peices around and end back on our side of the board    
        if (getNumStones(currentPit)==1){//if we are ending on a empty pit that WAS empty before we put one stone there
             //if we start and end on player1's side of the board and not a store
            if (playerNum==1 && currentPit >= 1 && currentPit < 7 && !isStore){
                numAddedToStore+=captureStones(currentPit);
            }
            //if we start and end on player2's side of the board
            if (playerNum==2 && currentPit > 6 && currentPit < 13 && !isStore){
                numAddedToStore+=captureStones(currentPit);
            }
        }

        stonesToStore= numAddedToStore;
        numAddedToStore=0;
        return stonesToStore;
    }

    @Override
    int distributeStones(int startPit) {
        if (startPit < 1 || startPit > 12) {
            throw new PitNotFoundException("Invalid pit number. Pit number must be in the range of 1-12.");
        }
        int currentPit = startPit;   
        int stonesToDistribute = getNumStones(currentPit);
        int stonesDisreibuted = stonesToDistribute;
        boolean endOnStore = false;


        if(getDataStructure().currentIteratorPostion()==-1){//if there is no current iterator running (moveStones was not called)
            getDataStructure().setIterator(startPit,getCurrentPlayer(),true);//make a new iterator
        }
        
        getDataStructure().removeStones(startPit);
        while (stonesToDistribute > 0) {
            //Move Iterator to the next respective pit/store
            getDataStructure().next();


            //IteratorPostion=0-13
            currentPit=getDataStructure().currentIteratorPostion();
            if (currentPit==6){//if the Iterator is pointing at player 1's store
                getDataStructure().addToStore(1,1);//add 1 stone to the store
                numAddedToStore++;
                endOnStore = true;
                stonesToDistribute--;
            }else if (currentPit==13){//if the Iterator is pointing at player 2's store
                getDataStructure().addToStore(2,1);//add 1 stone to the store
                numAddedToStore++;
                endOnStore = true;
                stonesToDistribute--;
            }else{
                if (currentPit<6){
                    currentPit++;//to convert 0-5 to 1-6 for pit numbers
                }
                getDataStructure().addStones(currentPit,1);
                endOnStore = false;
                stonesToDistribute--;
            }

        }

        
        return stonesDisreibuted;
    }

    @Override
    int captureStones(int stoppingPoint) {
        if (getNumStones(stoppingPoint)==1){
            int pitTakeFrom=0;
            int numCaptured=0;
            if (stoppingPoint < 1 || stoppingPoint > 12) {
                throw new PitNotFoundException("Invalid pit number. Pit number must be in the range of 1-12.");
            }else if (stoppingPoint < 7){//player 1 taking player 2's stones
                pitTakeFrom=13-stoppingPoint;//get oppside pit number

                //takes the stones out of the opponest pit and saves the number of stones taken
                numCaptured+=getDataStructure().removeStones(pitTakeFrom);
                getDataStructure().addToStore(1, numCaptured);//add the stones to player 1's store
                return numCaptured;
            }else {//player 2 taking player 1's stones
                pitTakeFrom=13-stoppingPoint;//get oppside pit number

                //takes the stones out of the opponest pit and saves the number of stones taken
                numCaptured+=getDataStructure().removeStones(pitTakeFrom);
                getDataStructure().addToStore(2, numCaptured);//add the stones to player 2's store
                return numCaptured;
            }
        }else{
            return 0;
        }
    }
}
