//This entire class is AI writen

package mancala;
import java.io.Serializable;

public class Player implements Serializable{
    private static final long serialVersionUID = 12L;
    private String name;
    private Store store;

    public Player() {// Default constructor
        name = "Player";
        store = new Store();
    }

    public Player(final String givenName) {
        this.name = givenName;
    }

    public Store getStore() {
        return store;
    }

    public String getName() {
        return name;
    }

    public void setName(final String givenName) {
        this.name = givenName;
    }

    public void setStore(final Store givenStore) {
        this.store = givenStore;
    }

    public int getStoreCount() {
        int storeCnt=0;
        if (store != null) {
            storeCnt=store.getTotalStones();
        } 
        return storeCnt;
    }
    @Override
    public String toString() {
        return name; 
    }
}
