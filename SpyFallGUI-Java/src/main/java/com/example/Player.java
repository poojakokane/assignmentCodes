package com.example;

public class Player {

    /*
     * In this class, only the name is considered for equality and hashCode
     * timesWon is not used for these methods
     * this means two players with the same name are equal, even if the number of wins is different
     */

    private String name;
    private int timesWon;

    public Player(String name) {
        this.name = name;
        timesWon = 0;
    }


    public void addWin() {
        timesWon++;
    }

    public String toString() {
        return name + ", Wins:" + timesWon;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Player))
            return false;
        Player other = (Player) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return timesWon;
    }




}
