package mancala;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AyoRulesTest {
    private AyoRules rules;
    private Player player1;
    private Player player2;
    private MancalaDataStructure data;

    @BeforeEach
    public void setUp() {
        /* the constructor calls whatever methods
        are needed to set up the board data structure */
        rules = new AyoRules(); 
        player1 = new Player();
        player2 = new Player();
        rules.registerPlayers(player1, player2); 
        data = rules.getDataStructure();
    }
    
      @Test
    public void testMoveStonesOneMove2() throws InvalidMoveException {
  
        //player one move stones starting at pit four
        int num = rules.moveStones(5,1);
        assertEquals(2,num);  //number in players store

        assertEquals(0,data.getNumStones(5));//as this is the starting pit
        assertEquals(6,data.getNumStones(6));//4 by def 2 for dis
        assertEquals(0,data.getNumStones(8));//as this is a pit we hit
        assertEquals(0,data.getNumStones(1));//as this is a pit we hit
        assertEquals(5,data.getNumStones(2));//passed by this so it should be +1
        
        
}
     
    


 
    @Test
    public void testMoveStonesOneMove() throws InvalidMoveException {

        data.removeStones(7);
        //player one move stones starting at pit four
        int num = rules.moveStones(4,1);
        assertEquals(1,num);  //number in players store
        /*other tests.   
        Pit 7 should also have 1 stone,  
        pit 5 should have 5 */

        assertEquals(5,data.getNumStones(5));
        assertEquals(1,data.getNumStones(7));
        
}
@Test
    public void testMoveStonesOneMove1() throws InvalidMoveException {

        data.removeStones(1);
        
        //player one move stones starting at pit four

        int num = rules.moveStones(5,1);
        assertEquals(0,data.getNumStones(5));//as this is the start pit and shold stay empty no matter what
        assertEquals(0,data.getNumStones(8));//as we distripute stones
        assertEquals(6,num);  //captuer 5, 1 from dis
        /*other tests.   
        Pit 7 should also have 1 stone,  
        pit 5 should have 5 */

        assertEquals(5,data.getNumStones(6));
        
        assertEquals(1,data.getNumStones(1));
        //assertEquals(1,data.getNumStones(6));
        
}

    @Test
    public void testMoveStonesMultiMove() throws InvalidMoveException {

        data.addStones(12,2);
        //player one move stones starting at pit one
        int num = rules.moveStones(4,1);

        // did we skip pit 4?
        assertEquals(0,data.getNumStones(4));    
        // is pit 12 empty?
        assertEquals(0,data.getNumStones(12));   
         //number in players store
        assertEquals(2,num); 
        // does pit 6 have 6 stones?
        assertEquals(6,data.getNumStones(6));
        // does pit 7 have 1 stone?
        assertEquals(1,data.getNumStones(7));   
        
         
              
  }
    @Test
    public void endOnStoreSimple() {
       int num = rules.moveStones(3,1);
      assertEquals(0,data.getNumStones(3));   
      assertEquals(1,num);   
    }

    @Test
    public void endOnStoreMid() {
      data.removeStones(2);//pit 2 will now have 0
      data.addStones(2,3);//pit 2 will now have 3
       int num = rules.moveStones(6,1);
      assertEquals(0,data.getNumStones(6));   //should stay empty as this is the start pit
      assertEquals(0,data.getNumStones(9));   //should stay empty as this is the start pit
      assertEquals(5,data.getNumStones(8));
      assertEquals(5,data.getNumStones(5));
      assertEquals(2,num);   
    }
    @Test
    public void endOnStoreCom() {
      data.addStones(9, 4);//pit 9 will now have 8
       int num = rules.moveStones(6,1);
      assertEquals(0,data.getNumStones(6));   //should stay empty as this is the start pit
      assertEquals(0,data.getNumStones(9));   //should stay empty as this is the start pit
      assertEquals(5,data.getNumStones(8));
      assertEquals(5,data.getNumStones(5));
      assertEquals(2,num);   
    }
  

    @Test
    public void testDistributeStonesSingleMove() {
        /*empty pit 9  setup*/
        data.removeStones(9);


        //distribute stones starting at pit 6
        int num = rules.distributeStones(6);
        assertEquals(4,num);  //number distributed
        //pit 6 has zero stones
        assertEquals(0,data.getNumStones(6));
        //pit 8 has 5 stones
        assertEquals(5,data.getNumStones(8));
        //pit 9 has 1 stones
        assertEquals(1,data.getNumStones(9));
}


    @Test
    public void testCaptureStonesNonEmptyTarget() {
  
         //remove all stones from pit 1
        data.removeStones(1);
        // add a single stone to pit 1 to simulate capture scenario
        data.addStones(1,1);
        //player one move stones starting at pit one
        rules.setPlayer(1);
        int num = rules.captureStones(1);
        // return value should be 4 as it is only stones captured
        assertEquals(4,num);
       //pit 12 should be empty
       assertEquals(0,data.getNumStones(12)); 
       //pit 1 should have a single stone
        assertEquals(1,data.getNumStones(1));
       /*not testing the player's store as it is not 
       the responsibility of capture stones to put stones
       in the store*/
}


}