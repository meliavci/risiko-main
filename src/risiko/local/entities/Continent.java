package risiko.local.entities;
import java.util.ArrayList;
import java.util.List;
public class Continent {
    String name;
    Player player;
    int bonusUnits;
    List<Country> countries;

    public Continent(String name, int bonusUnits){
        this.name = name;
        this.bonusUnits = bonusUnits;
        this.countries = new ArrayList<>();
        this.player = null;
    }

    public String getName(){
        return name;
    }

    public List<Country> getCountries(){
        return countries;
    }

    public String getCountryNames(){
        String countryNames = "";
        for(Country country : countries){
            countryNames += country.getName() + ", ";
        }
        return countryNames;
    }

    public int getBonusUnits(){
        return bonusUnits;
    }

    public void addCountry(Country country){
        countries.add(country);
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    public void removePlayer(){
        this.player = null;
    }

}
