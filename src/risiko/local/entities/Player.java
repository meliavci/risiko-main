package risiko.local.entities;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Player {
    private String name;
    private int id;
    private String color;
    private int numberOfUnits;
    private int numberOfCountries;
    private MissionCard missionCard ;
    private boolean isAlive;
    private List<Country> countries;


    public Player(String name, int id, String color) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.numberOfUnits = 0;
        this.numberOfCountries = 0;
        this.missionCard = null;
        this.isAlive = true;
        this.countries = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public int getNumberOfCountries() {
        return numberOfCountries;
    }

    public MissionCard getMissionKarten() {
        return missionCard;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public List<Country> getCountries() {
        return countries;
    }
    public String getCountryNames(){
        String countryNames = "";
        int index = 0;
        for(Country country : countries){
            countryNames += "> " + country.getName() + "\n";
        }
        return countryNames;
    }

    public void addCountry(Country country){
        countries.add(country);
    }

    public void removeCountry(Country country){
        countries.remove(country);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public void addUnits(int units) {
        this.numberOfUnits += units;
    }

    public void addNumberOfCountries(int numberOfCountries) {
        this.numberOfCountries += numberOfCountries;
    }
    public void removeNumberOfCountries(int numberOfCountries) {
        this.numberOfCountries -= numberOfCountries;
    }

    public void setMissionCard(MissionCard missionCard) {
        this.missionCard = missionCard;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public boolean ownsCountry(String country) {
        if (countries == null || country==null){
            return false;
        }
        for (Country c : countries) {
            if (c.getName().equalsIgnoreCase(country)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return getId() == player.getId() && getColor() == player.getColor() && Objects.equals(getName(), player.getName());
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(getId(), getColor(), getName());
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", color=" + color +
                ", numberOfUnits=" + numberOfUnits +
                ", numberOfCountries=" + numberOfCountries +
                ", missionCard=" + missionCard +
                ", isAlive=" + isAlive +
                ", lands=" + countries.toString() +
                '}';
    }
}
