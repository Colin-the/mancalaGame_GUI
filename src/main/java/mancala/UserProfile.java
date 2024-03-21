package mancala;

import java.io.Serializable;

public class UserProfile implements Serializable{
    private static final long serialVersionUID = 6L;
    private String name;
    private int numOfKGamesPlayed;
    private int numOfKGamesWon;
    private int numberOfAyoPlayed;
    private int numberOfAyoWon;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getNumberOfKalahGamesPlayed() {
        return numOfKGamesPlayed;
    }

    public void setNumberOfKalahGamesPlayed(final int numOfKGamesPlayed) {
        this.numOfKGamesPlayed = numOfKGamesPlayed;
    }

    public int getNumberOfKalahGamesWon() {
        return numOfKGamesWon;
    }

    public void setNumberOfKalahGamesWon(final int numOfKGamesWon) {
        this.numOfKGamesWon = numOfKGamesWon;
    }

    public int getNumberOfAyoPlayed() {
        return numberOfAyoPlayed;
    }

    public void setNumberOfAyoPlayed(final int numberOfAyoPlayed) {
        this.numberOfAyoPlayed = numberOfAyoPlayed;
    }

    public int getNumberOfAyoWon() {
        return numberOfAyoWon;
    }

    public void setNumberOfAyoWon(final int numberOfAyoWon) {
        this.numberOfAyoWon = numberOfAyoWon;
    }
}
