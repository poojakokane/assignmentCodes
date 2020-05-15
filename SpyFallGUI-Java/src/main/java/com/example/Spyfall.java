package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * Maintains information about a game of spyfall
 * It uses a map to keep track of what role each player has
 * It uses another map to keep track of which roles exist for each possible location
 */

public class Spyfall {



    private final static String[] locationList = {"Ski Lodge","Bank","Beach"};
    private final Role spy = new Role();
    private Map<String, String[]> locationMap;
    private Map<Player,Role> playerMap;



    /**
     * initialize both maps, and populate the locationMap with the corresponding String array of roles.
     * mapping locations to the list of roles they have.
     */
    public Spyfall() {
        playerMap = new HashMap<Player, Role>();
        locationMap = generateLocationMap();
    }

    private static Map<String, String[]> generateLocationMap() {
        String[] skiLodge = {"Ski Patrol", "Lift Technician", "Tourist", "Ski Instructor", "Owner"};
        String[] bank = {"Teller", "Robber", "Customer", "Custodian", "Owner"};
        String[] beach = {"Surfer", "Lifeguard", "Tourist", "Fisher", "Park Ranger"};

        //create a hashmap of string to string array
        //it should map each location to the array of roles for that location
        //return the map after storing the associations

        HashMap<String, String[]>locationMap = new HashMap<String,String[]>();

        locationMap.put(locationList[0], skiLodge);
        locationMap.put(locationList[1], bank);
        locationMap.put(locationList[2], beach);

        return locationMap;
    }



    /**
     * clears the playerMap to be ready to start a new game.
     */
    public void resetGame() {
        //call clear on the player map
        playerMap.clear();
    }

    /**
     * checks to see if the given location is in the locationMap.
     * @param location
     * @return whether the location is in the locationMap
     */
    public boolean containsLocation(String location) {
        //return true iff a player with the given name is in the player map
        return locationMap.containsKey(location);
    }

    /**
     * checks to see if the given player is in the playerMap.
     * @param player
     * @return whether the player is in the playerMap
     */
    public boolean containsPlayer(Player player) {
        //return true iff a player with the given name is in the player map
        return playerMap.containsKey(player);
    }

    /**
     * let a player check what role they received
     * @param player
     * @return the role assigned to that player
     */
    public Role getRole(Player player) {
        //return the role for that player, or null if that player has no role
        return playerMap.get(player);
    }

    //A NEW method that helps in the playerPanels
    public String getPlayersRole(Player p) {
        if(playerMap.containsKey(p)) {
            return this.playerMap.get(p).toString();
        }
        return("This player has no Role yet");
    }



    /**
     * starts a new game of 'spyfall' with the players inside the players array. To start a game first
     * assign a spy, and then assign normal roles.
     * @param players
     * @throws IllegalArgumentException if players is null or empty.
     */
    public void startGame(ArrayList<Player> players) {
        if(players == null) {
            throw new IllegalArgumentException("players cannot be null");
        }
        if(players.isEmpty()) {
            throw new IllegalArgumentException("players cannot be empty");
        }
        assignSpy(players);

        assignNormalRoles(players);
    }


    /**
     * randomly selects one of the players to become the spy. should use the PlayerMap to map that Player
     * to the role of spy. This method should be called directly before assignNormalRoles is called.
     * @param players
     */
    public void assignSpy(ArrayList<Player> players) {
        //choose a player randomly
        //assign that player the spy role in the player map
        int spyNum = (int)(Math.random() * players.size());

        playerMap.put(players.get(spyNum), spy);
    }




    /**
     * assigns roles to the rest of the players. this method should be called directly after assignSpy is called.
     * That also means before this method is called, there is exactly one player (the spy) inside the playerMap.
     * use a random number to get the name of a location, then use the locationMap to get the list of roles.
     * use this list to give the remaining players each a different role.
     * @param players
     */
    public void assignNormalRoles(ArrayList<Player> players){
        //randomly choose one of the locations from locationList
        //for the chosen location, get the corresponding array of roles from the location map
        //give each player a role from that array (in the player map)
        //UNLESS that player is already the spy
        //if there are as many non-spy players as roles (or fewer players) this should give each player a different role
        //if there are more, you can repeat roles

        int roleNum = (int)(Math.random() * ((locationList.length-1) + 1));


        String[] roleList = locationMap.get(locationList[roleNum]);
        int count = 0;
        for(Player p :players) {

            if(playerMap.containsKey(p)) {
                //do nothing they are the spy
            }else {
                playerMap.put(p, new Role(locationList[roleNum], roleList[count]));
                count++;
                if(count >= roleList.length)
                    count -= roleList.length;
            }
        }
    }







    /**
     * checks if the player was the spy, and if they were, all the players who were not the spy win.
     * if the guessed player was not the spy, the spy wins.
     * (remember to addWin() to all the wining players)
     * Then reset the game.
     * @param player the player guessed
     * @throws IllegalArgumentException if the player given was not in the playerMap.
     * @return A String saying who won.
     */
    public String checkIfSpy(Player player) {
        //make sure the player map contains this player
        //check if the player has the spy role
        //if so, add a win for each non-spy in the map (use keySet)
        //	and return "The Non-Spies Win!"
        //if not, add a win for the spy (again use keySet - this operation is not efficient)
        //  return "The Spy Wins!"
        //either way, reset the game before returning
        if(!playerMap.containsKey(player))
            throw new IllegalArgumentException("player named " + player.getName() + " could not be found");

        if(playerMap.get(player).equals(spy)) {

            for (Player person : playerMap.keySet()) {
                if(!playerMap.get(person).equals(spy)) {
                    person.addWin();
                }
            }
            resetGame();
            return "The Non-Spies Win!";
            //add a win to all the non spies
        } else {
            for (Player person : playerMap.keySet()) {
                if(playerMap.get(person).equals(spy)) {
                    person.addWin();
                }
            }
            resetGame();
            return "The Spy Wins!";
            //add a win to the spy
        }
    }



    /**
     * prints out all the players and their roles.
     */
    public String toString() {
        if(playerMap.isEmpty())
            return "The game has not started yet";
        else {
            String output = "";
            for (Player person : playerMap.keySet()) {
                output = output + person.toString() + " " + playerMap.get(person).toString()+ "\n";
            }
            return output;
        }
    }


    public void clearPlayerMap() {
        this.playerMap.clear();
    }
}

