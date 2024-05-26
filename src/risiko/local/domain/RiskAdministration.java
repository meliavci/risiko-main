package risiko.local.domain;
import risiko.local.entities.*;
import risiko.local.exceptions.CountryNotFoundException;
import risiko.local.exceptions.InvalidUnitException;
import risiko.local.exceptions.NotANeighbourCountry;
import risiko.local.exceptions.PlayerNotFoundException;
import risiko.local.persistence.CountryInitiator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("LanguageDetectionInspection")
public class RiskAdministration {

    private List<String> availableColors = new ArrayList<>(Arrays.asList("Red", "Blue", "Green", "Yellow", "Black", "White"));
    final private List<Country> countries;
    final private List<Continent> continents;
    final private List<Player> players;
    public List<MissionCard> missionCards;
    final private Dice dice;
    public CountryInitiator countryInitiator;
    final private List<Country> availableCountries;
    private int playerTurnIndex;
    private static final int MAX_ATTACK_UNITS = 3;
    private static final int MAX_DEFENDER_DICE = 2;


    public RiskAdministration(String filePathCountries, String filePathContinent){
        this.countryInitiator = new CountryInitiator(filePathCountries);
        this.countries = countryInitiator.initializeCountries();
        this.continents = countryInitiator.initializeContinents(filePathContinent, countries);
        this.players = new ArrayList<>();
        this.missionCards = new ArrayList<>();
        this.availableCountries = new ArrayList<>(countries);
        this.dice = new Dice();
        playerTurnIndex = 0;
    }

    /**
     * Returns a String of all Players
     * @return String of Players
     */
    public String listPlayers(){
        String playerList = "";
        for (Player player : players){
            playerList += "Name: " +player.getName() + " ID: " + player.getId() + " Color: " + player.getColor() + "\n";
        }
        return playerList;
    }

    /**
     * gets the current player
     * @return returns current player object
     */
    public Player getPlayerOnTurn(){
        return players.get(playerTurnIndex);
    }

    /**
     * Sets the nextTurnIndex to the next player
     * @author Vincent
     */
    public void nextTurn(){
        if (playerTurnIndex == players.size() -1) {
            playerTurnIndex = 0;
        } else {
            playerTurnIndex++;
        }
    }

    /**
     * Adds a player to the game
     * @author Melisa
     * @param name Name of the player
     * @param playerId ID of the player
     * @param colorName Color of the player
     * @return Player
     */
    public Player addPlayer(String name, int playerId, String colorName){
        if (!availableColors.contains(colorName)){
            throw new IllegalArgumentException("Color " + colorName + " has already been selected by another player or is invalid.");
        }

        Player player = new Player(name, playerId, colorName);
        players.add(player);
        availableColors.remove(colorName);
        return player;
    }

    /**
     * Removes a player from the game
     * @author Melisa
     * @param name Name of the player
     * @return Player
     */
    public Player removePlayer(String name) throws PlayerNotFoundException {
        Player player = getPlayerByName(name);
        if (player == null){
            throw new PlayerNotFoundException(name);
        }
        players.remove(player);
        availableColors.add(player.getColor()); // Add the color back to the available colors list
        return player;
    }

    public Player getPlayer(int playerIndex) {
        return players.get(playerIndex);
    }

    /**
     * Searches for a Country Object by its name
     * @author Vincent
     * @return String of Countries
     */
    public Country getCountryByName(String countryName) throws CountryNotFoundException {
        for (Country country : countries) {
            if (country.getName().equalsIgnoreCase(countryName)) {
                return country;
            }
        } throw new CountryNotFoundException(countryName);
    }

    public String listAllCountries(){
        String countryList = "";
        for (Country country : countries) {
            countryList += country.getName() + "\n";
        }
        return countryList;
    }

    /**
     * Searches for a Player by its Name
     * @author Vincent
     * @param playerName name of the Player
     * @return returns the player object with the given name
     */
    public Player getPlayerByName(String playerName){
        for (Player player : players){
            if (player.getName().equals(playerName)){
                return player;
            }
        }
        return null;
    }

    /**
     * Searches for a Player by its Color
     * @author Melisa
     * @param color Color of the Player
     * @return returns the player object with the given color
     */
    public Player getPlayerByColor(String color){
        for (Player player : players){
            if (player.getColor().equals(color)){
                return player;
            }
        }
        return null;
    }

    /**
     * Searches for a Player by its ID
     * @author Vincent
     * @param id Player ID
     * @return returns the player object with the given id
     */
    public Player getPlayerById(int id){
        for (Player player : players){
            if (player.getId() == id){
                return player;
            }
        }
        return null;
    }

    /**
     * Adds a country to a player, used for GameStart() but I don't know if it's good this way, maybe other name I guess
     * @author Vincent
     * @param countryName Name of the country
     * @param playerObject Player Object, gets set in CUI
     */
    public String addCountryToPlayer(String countryName, Player playerObject) throws CountryNotFoundException {
        if (playerObject != null) {
            Country country = getCountryByName(countryName);
            playerObject.addCountry(country);
            playerObject.addNumberOfCountries(1);
            country.setPlayer(playerObject);
            country.addUnits(1);
            return ("Country " + countryName + " has been added to Player " + playerObject.getName());
        } else {
            return ("Player Object is null");
        }
    }

    /**
     * checks if a player owns a continent (tested and fixed)
     * @author Melisa
     * @param player Player
     *
     */
    public String continentPlayerCheck(Player player) {
        boolean ownsContinent;
        StringBuilder result = new StringBuilder();
        for (Continent continent : continents) {
            ownsContinent = true;
            for (Country country : continent.getCountries()){
                if (country.getPlayer() != player) {
                    ownsContinent = false;
                    break;
                }
            }
            if (!ownsContinent) continue;
            continent.setPlayer(player);
            result.append("Player ").append(player.getName()).append(" has conquered ").append(continent.getName()).append(" and gets ").append(continent.getBonusUnits()).append(" Bonus units.\n");
        }
        return result.toString();
    }

    /**
     * Removes a country from a player
     * @author Melisa
     * @param countryName Name of the country
     * @param playerName Player Object, gets set in CUI
     */
    public String removeCountryFromPlayer(String countryName, String playerName) throws CountryNotFoundException {
        Country country = getCountryByName(countryName);
        Player player = getPlayerByName(playerName);
        player.removeCountry(country);
        player.removeNumberOfCountries(1);
        return "Country " + countryName + " has been removed from Player " + playerName;
    }

    /**
     * Returns a String of all available countries used in CUI GameStart()
     * @author Vincent
     * @return String of available countries
     */
    public String getStringAvailableCountries(){
        String availableCountriesString = "";
        for (Country country : availableCountries){
            availableCountriesString += country.getName() + "\n";
        }
        return availableCountriesString;
    }

    /**
     * Returns a List of all available countries used in CUI GameStart()
     * @author Vincent
     * @return List of available countries
     */
    public List<Country> getAvailableCountries(){
        return availableCountries;
    }


    /**
     * Picks random countries for the players at the start of the game
     * @author Vincent
     *
     */
    public String pickRandomStartCountries() throws CountryNotFoundException {
        while (!availableCountries.isEmpty()) {
            for (Player player : players) {
                if (!availableCountries.isEmpty()) {
                    int randomIndex = (int) (Math.random() * availableCountries.size());
                    Country randomCountry = availableCountries.get(randomIndex);
                    addCountryToPlayer(randomCountry.getName(), player);
                    randomCountry.setPlayer(player);
                    availableCountries.remove(randomCountry);
                }
            }
        }
        return "All countries have been assigned to players";
    }

    /**
     * Adds the Units to the Player depending on its owned countries and continent bonuses
     * @author Vincent
     * @param player Player
     * @return Returns string of available Units to place
     */
    public String addPlayerUnitsPerRound(Player player){
        int units;
        if (player.getCountries().size() >= 9) {
            units = player.getCountries().size()/3;
            for (Continent continent : continents) {
                if (continent.getPlayer() == player) {
                    units += continent.getBonusUnits();
                }
            }
        } else {
            units = 3;
        }
        player.addUnits(units);
        return ("Player " + player.getName() + " has " + player.getNumberOfUnits() + " units to place");
    }

    /**
     * places Units on a picked country
     * @author Vincent
     * @param player Player Object of which players turn it is
     * @param pCountry Country choosen by the player
     * @param units amount of Units the player wants to place
     * @return returns a String for output in the CUI
     */
    public String addPlayerUnitsToCountry (Player player, String pCountry, int units) throws InvalidUnitException {
        try {
            Country country = getCountryByName(pCountry);
            if (country.getPlayer() == player && player.getNumberOfUnits() >= units) {
                country.addUnits(units);
                player.setNumberOfUnits(player.getNumberOfUnits() - units);
                return ("Player " + player.getName() + " has placed " + units + " units in " + country.getName());
            } else if (country.getPlayer() != player){
                return ("Country " + pCountry + " does not belong to Player " + player.getName());
            } else {
                throw new InvalidUnitException("Player " + player.getName() + " has insufficient Units. Available units: " + player.getNumberOfUnits());
            }
        } catch (CountryNotFoundException e) {
            return e.getMessage();
        }
    }

    /**
     * @author Melisa
     * This method allows a player to attack a territory from another territory.
     * @param fromCou, toCou, attacker, numberOfUnits
     */
    public void makeAttack(Country fromCou, Country toCou, Player attacker, int numberOfUnits, int defenderUnits) throws IllegalStateException, CountryNotFoundException, NotANeighbourCountry {
        Country from = getCountryByName(fromCou.getName());
        Country to = getCountryByName(toCou.getName());
        validateAttackConditions(from, to, attacker, numberOfUnits, defenderUnits);

        Integer[] attackerRolls = rollAttackerDice(numberOfUnits);
        Integer[] defenderRolls = rollDefenderDice(defenderUnits);

        resolveDiceRolls(from, to, numberOfUnits, attackerRolls, defenderRolls);

    }
    /**
     * @author Yvann
     * @param numberOfUnits
     * This method resolves the dice rolls of the attacker.
     */
    private Integer[] rollAttackerDice(int numberOfUnits) {
        return dice.rollDice(numberOfUnits);
    }
    /**
     * @author Yvann
     * @param numberOfUnits
     * This method resolves the dice rolls of the defender.
     */
    private Integer[] rollDefenderDice(int numberOfUnits) {
        return dice.rollDice(Math.min(numberOfUnits, MAX_DEFENDER_DICE));
    }
    /**
     * @param attackerRolls, defenderRolls, attackerLossesUnits, defenderLossesUnits
     * This method resolves the dice rolls of the attacker and the defender.
     * @author Melisa
     */
    private void resolveDiceRolls(Country from, Country to, int numberOfUnits, Integer[] attackerRolls, Integer[] defenderRolls) {
        System.out.println("attackerRolls = " + Arrays.toString(attackerRolls));
        System.out.println("defenderRolls = " + Arrays.toString(defenderRolls));
        int attackerLossesUnits = 0, defenderLossesUnits = 0;
        for (int i = 0; i < Math.min(attackerRolls.length, defenderRolls.length); i++) {
            if (attackerRolls[i] > defenderRolls[i]) {
                defenderLossesUnits++;
            } else {
                attackerLossesUnits++;
            }
        }

        updateTerritoryUnits(from, to, numberOfUnits, attackerLossesUnits, defenderLossesUnits);
    }
    /**
     * @author Yvann
     * @param from, to, numberOfUnits, attackerLossesUnits, defenderLossesUnits
     * This method updates the units in the territories after an attack.
     */
    private void updateTerritoryUnits(Country from, Country to, int numberOfUnits, int attackerLossesUnits, int defenderLossesUnits) {
        //Defender losses
        if(defenderLossesUnits > 0){
            resolveSuccessfulAttack(from, to, attackerLossesUnits , defenderLossesUnits);
        }else {
            resolveFailedAttack(from, to, attackerLossesUnits, defenderLossesUnits);
        }
        //Update Units in the Territory
        conquerTerritory(from, to, numberOfUnits);
    }
    /**
     * @author Yvann
     * @param numberOfUnits, defendingCountry
     * This method allows a player to defend a territory with a specified number of units.
     */
    public void defend(Country defendingCountry, int numberOfUnits) throws IllegalStateException {
        if(numberOfUnits < 1 || numberOfUnits > 2){
            throw new IllegalArgumentException("The number of units for defense must be between 1 and 2.");
        }
        if(defendingCountry.getUnits() < 1){
            throw new IllegalArgumentException("Country has no units to defend.");
        }

        defendingCountry.removeUnits(numberOfUnits);
    }

    /**
     * @author Melisa
     * This method validates the conditions for an attack.
     * @param from, to, attacker, numberOfUnits, defenderUnits
     */
    public void validateAttackConditions(Country from, Country to, Player attacker, int numberOfUnits, int defenderUnits) throws IllegalStateException, NotANeighbourCountry {
        validateOwnership(from.getName(), attacker);
        validateNeighboring(from, to);
        validateSufficientUnits(from, numberOfUnits);
        validateNotOwnCountry(attacker, to);
        validateAttackUnits(numberOfUnits);
        validateDefenderUnits(defenderUnits);
    }

    /**
     * @author Melisa
     * @param numberOfUnits
     * This method validates the number of dice for an attack.
     */
    public boolean validateDefenderUnits(int numberOfUnits) {
        return numberOfUnits <= MAX_DEFENDER_DICE && numberOfUnits >= 1;
    }

    /**
     * @author Yvann
     * @param numberOfUnits, from
     * This method validates the number of units for an attack.
     */
    public boolean validateAttackUnits(int numberOfUnits)  {
        return numberOfUnits <= MAX_ATTACK_UNITS && numberOfUnits >= 1;
    }
    /**
     * @author Yvann
     * @param numberOfUnits, from
     * This method validates the number of units for an attack.
     */
    public boolean validateRestOfUnitWhenAttack(Country from, int numberOfUnits)  {
        if(from.getUnits() - numberOfUnits < 1){
            throw new IllegalStateException("At least one unit must remain in the territory.");
        }
        return true;
    }

    /**
     * @author Yvann
     * @param attacker, country
     * This method validates the ownership of a territory by a player.
     */
    public boolean validateOwnership(String countryName, Player attacker) {
        return attacker.ownsCountry(countryName);
    }
    /**
     * @author Melisa
     * @param from, to
     * This method validates the neighboring of a territory.
     */
    public boolean validateNeighboring(Country from, Country to) throws NotANeighbourCountry {
        if (!from.getNeighbors().contains(to)) {
            throw new NotANeighbourCountry(to.getName());
        }
        return true;
    }
    /**
     * @author Yvann
     * @param country, numberOfUnits
     * This method validates the number of units for an attack.
     */
    public void validateSufficientUnits(Country country, int numberOfUnits) {
        if (country.getUnits() < numberOfUnits || numberOfUnits <= 0) {
            throw new IllegalStateException("The player does not have enough units to move the specified number.");
        }
    }
    /**
     * @author Yvann
     * @param attacker, defenderCountry
     * This method validates that the player does not attack their own territory.
     */
    public void validateNotOwnCountry(Player attacker, Country defenderCountry) {
        if (defenderCountry.getPlayer().getId() == attacker.getId()) {
            throw new IllegalStateException("The player cannot attack their own territory.");
        }
    }
    /**
     * @param attackerCountry, defenderCountry, defenderLossesUnits
     *                         This method resolves a successful attack.
     * @author Melisa
     */
    public void resolveSuccessfulAttack(Country attackerCountry, Country defenderCountry, int attackerLossesUnits, int defenderLossesUnits){
        defenderCountry.removeUnits(defenderLossesUnits);
        attackerCountry.removeUnits(attackerLossesUnits);
        System.out.println("Attack successful! The Territory " + defenderCountry.getName() + " has been conquered.");
        System.out.println(attackerCountry.getName() + " has conquered " + defenderCountry.getName());
        System.out.println(attackerCountry.getName() + " lost " + attackerLossesUnits + " units." + " and " + defenderCountry.getName() + " lost " + defenderLossesUnits + " units.");
    }
    /**
     * @param attackerCountry, defenderCountry, attackerLossesUnits
     *                         This method resolves a failed attack.
     * @author Melisa
     */
    public void resolveFailedAttack(Country attackerCountry, Country defenderCountry, int attackerLossesUnits, int defenderLossesUnits) {
        attackerCountry.removeUnits(attackerLossesUnits);
        System.out.println("Attack failed! The Territory " + defenderCountry.getName() + " has not been conquered.");
        System.out.println(attackerCountry.getName() + " lost " + attackerLossesUnits + " units." + " and " + defenderCountry.getName() + " lost " + defenderLossesUnits + " units.");
    }
    /**
     * @author Melisa
     * @param attackerCountry, defenderCountry, numberOfUnits
     * This method resolves a failed attack.
     */
    public void conquerTerritory(Country attackerCountry, Country defenderCountry, int numberOfUnits) {
        if (defenderCountry.getUnits() == 0) {
            //The territory of the defender is conquered
            //The attacker moves the units to the conquered territory
            int unitsAttacker = attackerCountry.getUnits() - numberOfUnits;
            defenderCountry.setPlayer(attackerCountry.getPlayer());
            defenderCountry.setUnits(numberOfUnits);
            attackerCountry.setUnits(unitsAttacker);
        }
    }

    /**
     * Moves units from one country to another
     * @author Melisa
     * @param player, fromCountry, toCountry, units
     */
    public String moveUnits(Player player, Country fromCountry, Country toCountry, int units){
        if (fromCountry.getPlayer() == player && toCountry.getPlayer() == player){
            if (fromCountry.getUnits() >= units + 1){ // Ensure at least one unit remains
                if (!fromCountry.getUnitsMoved()){ // Check if units have been moved this round
                    fromCountry.removeUnits(units);
                    toCountry.addUnits(units);
                    toCountry.setUnitsMoved(true); // Mark units as moved for this round
                    return "Player " + player.getName() + " has moved " + units + " units from " + fromCountry.getName() + " to " + toCountry.getName();
                } else {
                    return "Units in " + fromCountry.getName() + " have already been moved this round";
                }
            } else {
                return "Not enough units in " + fromCountry.getName() + " or cannot leave country empty";
            }
        } else {
            return "Countries do not belong to the same player";
        }
    }

    /**
     * Returns the information of a country in a formatted string
     * @author Melisa
     * @param countryName Name of the country
     * @return String of country information
     */
    public String getCountryInformation(String countryName) {
        try {
            Country country = getCountryByName(countryName);
            return "Country: " + country.getName() + "\n" +
                    "Owner: " + country.getPlayer().getName() + "\n" +
                    "Units: " + country.getUnits() + "\n" +
                    "Neighboring Countries: " + country.getNeighborNames();
        } catch (CountryNotFoundException e) {
            return "Country " + countryName + " does not exist";
        }
    }

    /**
     * Returns the available colors for the players to choose from
     * @author Melisa
     * @return String of available colors
     */
    public String getAvailableColors() {
        return availableColors.toString();
    }


}
