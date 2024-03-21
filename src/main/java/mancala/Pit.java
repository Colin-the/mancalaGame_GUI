//This entire class is AI writen

package mancala;

import java.io.Serializable;

public class Pit implements Countable, Serializable{
    private static final long serialVersionUID = 11;
    private int stoneCount;

    @Override
    public int getStoneCount() {
        return stoneCount;
    }
    @Override
    public void addStone() {
        stoneCount++;
    }
    @Override
    public void addStones(final int numToAdd) {
        stoneCount += numToAdd;
    }
    @Override
    public int removeStones() {
        final int removedStones = stoneCount;
        stoneCount = 0;
        return removedStones;
    }
}
