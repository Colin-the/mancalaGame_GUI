//This entire class is AI writen

package mancala;
import java.io.Serializable;

public class Store implements Countable, Serializable{ 
    private static final long serialVersionUID = 10L;
    private Player owner;
    private int stoneCount=0;

    public void setOwner(final Player player) {
        owner = player;
        if (owner != null) {
            owner.setStore(this);  // Associate the store with the player
        }
    }

    public Player getOwner() {
        return owner;
    }
    @Override
    public void addStone() {
        addStones(1);
    }

    @Override
    public void addStones(final int amount) {
        stoneCount += amount;
    }
    @Override
    public int getStoneCount() {
        return stoneCount;
    }
    public int getTotalStones() {
        return stoneCount;
    }
    @Override
    public int removeStones(){
        return emptyStore();
    }

    public int emptyStore() {
        final int removedStones = stoneCount;
        stoneCount = 0;
        return removedStones;
    }
}
