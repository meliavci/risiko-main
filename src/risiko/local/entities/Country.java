package risiko.local.entities;

import java.util.List;

public class Country {
    String name;
    int units;
    Player player;
    boolean hasPlayer;
    String shortName;
    List<Country> neighbors;

    public Country(String name, String shortName){
        this.name = name;
        this.shortName = shortName;
        this.player = null;
        this.hasPlayer = false;
        this.units = 0;
        this.neighbors = null;
    }

    public String getName(){
        return name;
    }


    public int getUnits(){
        return units;
    }

    // Missing Player Class, until then marked as Comment. DON'T FORGET!!!!
    public void addUnits(int units){
        this.units += units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void removeUnits(int units){
        this.units -= units;
    }

    public Player getPlayer(){
        return player;
    }

    public void setPlayer(Player player){
        hasPlayer = true;
        this.player = player;
    }

    public String getShortName(){
        return shortName;
    }

    //gets neighboring states
    public List<Country> getNeighbors(){
        return neighbors;
    }

    public Country getNeighborByName(String neighborName){
        for(Country neighbor : neighbors){
            if(neighbor.getName().equals(neighborName)){
                return neighbor;
            }
        }
        return null;
    }


    public String getNeighborNames(){
        String neighborNames = "";
        for(Country neighbor : neighbors){
            neighborNames += neighbor.getName() + ", " + neighbor.getShortName() + "\n";
        }
        return neighborNames;
    }

    public boolean isNeighbor(Country country) {
        if (country != null && country.getNeighbors() != null) {
            return country.getNeighbors().contains(this);
        }
        return false;
    }



    public void setNeighbors(List<Country> neighbors){
        this.neighbors = neighbors;
    }

    public boolean hasPlayer(){
        return hasPlayer;
    }

    public void removeArmy(int anzahlArmyToRemove) {
        if(anzahlArmyToRemove <=0 ||  anzahlArmyToRemove > units){
            throw new IllegalArgumentException("Invalid number of units to remove");
        }
        this.units -= units;
    }

    public boolean getUnitsMoved() {
        return false;
    }

    public void setUnitsMoved(boolean b) {
    }

    @Override
    public String toString() {
        return name ;
    }
}
