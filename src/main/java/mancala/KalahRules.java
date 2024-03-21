package mancala;

public class KalahRules extends GameRules{
    private static final long serialVersionUID = 5L;

    // Call the constructor of the superclass (GameRules)
    public KalahRules() {
        super(); 
    }

    @Override
    public int moveStones(final int startPit, final int playerNum) {
        if (startPit > 12 || startPit < 1){//if the pit number does not exsist (not betewen 1 and 12)
            throw new InvalidMoveException("Invalid move. This pit does not exist!");
        } else if (playerNum == 1 && startPit > 6 || playerNum == 2 && startPit < 7) {
            //if the player is trying to move stones they are not allowed to (player 1 tring to move player 2's stones)
            throw new InvalidMoveException("Invalid move. You are not allowed to move stones in the given pit.");
        }
        
        int stonesToStore=0;
        int stonesInPit = getNumStones(startPit);
        if (playerNum==1){
            while(startPit+stonesInPit>=7){
                stonesToStore++;//add a stone to the players store
                stonesInPit=stonesInPit-13;//take away the number of stones that a full lap around the board would take
            }
            getDataStructure().addToStore(1,stonesToStore);
            
        }else if (playerNum==2){//for player 2
            while(startPit+stonesInPit>=13){
                stonesToStore++;//add a stone to the players store
                stonesInPit=stonesInPit-13;//take away the number of stones that a full lap around the board would take
            }
            
            getDataStructure().addToStore(2,stonesToStore);
        }
        

        
        distributeStones(startPit);//distrbute the stones accros the board
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

        getDataStructure().removeStones(currentPit);
        //pits.get(currentPit-1).removeStones();//take all the stones out of the current pit

        boolean endOnStore = false;
        //we need to leave this outside the loop as it will take the stone for the 2nd players store out at the start
        if (currentPit==12){
            stonesToDistribute--;//this is the stone that would be going into the players store
            endOnStore=true;
        }
        while (stonesToDistribute > 0) {
            //this will move the current pit in the sequence 1,2,3...,11,12,1,2...
            currentPit = (currentPit + 1) % 12; 
            if (currentPit==0){
                currentPit=12;
            }  
            //if its player 1 moveing and we are at player 1's store
            if (currentPit == 7 && startPit > 1 && startPit <= 6) {
                stonesToDistribute--;//this is the stone that would be going into the players store 
                endOnStore=true;//presume this is the last stone
            }
            //make sure we have a stone to put in the next pit and it just didnt get taken by the store
            if (stonesToDistribute > 0){
                endOnStore=false;//as the last stone is going into a pit if we reach this case

                getDataStructure().addStones(currentPit, 1);
                //pits.get(currentPit-1).addStone();//add a stone to the pit and then take it away from the counter

                stonesToDistribute--;
            }
            //if its player 2 moveing and we are at player 2's store
            if (currentPit == 12 && startPit > 6 && startPit <= 12 && stonesToDistribute!=0) {
                stonesToDistribute--;//this is the stone that would be going into the players store
                endOnStore=true; 
            }          
        }
        //only want to call captureStones IF we have moved peices around and end back on our side of the board    
        if (getNumStones(currentPit)==1){//if we are ending on a empty pit that WAS empty before we put one stone there
             //if we start and end on player1's side of the board and not a store
            if (startPit >= 1 && startPit < 7 && currentPit >= 1 && currentPit < 7 && !endOnStore){
                captureStones(currentPit);
            }
            //if we start and end on player2's side of the board
            if (startPit > 6 && startPit < 13 && currentPit > 6 && currentPit < 13 && !endOnStore){
                captureStones(currentPit);
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
                numCaptured+=getDataStructure().removeStones(stoppingPoint);//get rid of the stone we used to capture
                getDataStructure().addToStore(1, 6);//add the stones to player 1's store
                return numCaptured;
            }else {//player 2 taking player 1's stones
                pitTakeFrom=13-stoppingPoint;//get oppside pit number

                //takes the stones out of the opponest pit and saves the number of stones taken
                numCaptured+=getDataStructure().removeStones(pitTakeFrom);
                numCaptured+=getDataStructure().removeStones(stoppingPoint);//get rid of the stone we used to capture
                getDataStructure().addToStore(2, numCaptured);//add the stones to player 2's store
                return numCaptured;
            }
        }else{
            return 0;
        }     
    }

    
}
