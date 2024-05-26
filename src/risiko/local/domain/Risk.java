package risiko.local.domain;
import risiko.local.entities.*;
import risiko.local.exceptions.CountryNotFoundException;
import risiko.local.exceptions.InvalidUnitException;
import risiko.local.exceptions.NotANeighbourCountry;
import risiko.local.exceptions.PlayerNotFoundException;

import java.io.IOException;
import java.util.List;
public class Risk {
    private String data = "";

    private RiskAdministration riskAdmin;

    public Risk (String data){
        this.data = data;
        riskAdmin = new RiskAdministration(data + "_Cou.txt", data + "_Con.txt");
    }

    public String getStringAvailableCountries(){
        return (riskAdmin.getStringAvailableCountries());
    }

    public String listPlayers(){
        return (riskAdmin.listPlayers());
    }

    public Player addPlayer(String name, int playerId, String color){
        return (riskAdmin.addPlayer(name, playerId, color));
    }

    public Player removePlayer(String name) throws PlayerNotFoundException {
        return (riskAdmin.removePlayer(name));
    }

    public Country getCountryByName(String countryName) throws CountryNotFoundException {
        return (riskAdmin.getCountryByName(countryName));
    }

    public Player getPlayerByName(String playerName){
        return (riskAdmin.getPlayerByName(playerName));
    }

    public List<Country> getAvailableCountries(){
        return (riskAdmin.getAvailableCountries());
    }

    public String addCountryToPlayer(String countryName, Player player) throws CountryNotFoundException {
        return riskAdmin.addCountryToPlayer(countryName, player);
    }

    public String pickRandomStartCountries() throws CountryNotFoundException {
        return riskAdmin.pickRandomStartCountries();
    }

    public Player getPlayerById(int playerId){
        return (riskAdmin.getPlayerById(playerId));
    }

    public void removeCountryFromPlayer(String countryName, String player) throws CountryNotFoundException {
        riskAdmin.removeCountryFromPlayer(countryName, player);
    }

    public String addPlayerUnitsPerRound(Player player){
        return riskAdmin.addPlayerUnitsPerRound(player);
    }

    public String addPlayerUnitsToCountry(Player player, String countryName, int units) throws InvalidUnitException {
        return riskAdmin.addPlayerUnitsToCountry(player, countryName, units);
    }

    public String listAllCountries() {
        return riskAdmin.listAllCountries();
    }

    public Player getPlayer(int playerIndex){
        return riskAdmin.getPlayer(playerIndex);
    }

    public Player getPlayerOnTurn(){
        return riskAdmin.getPlayerOnTurn();
    }

    public void nextTurn(){
        riskAdmin.nextTurn();
    }

    public void makeAttack(Country from, Country to, Player player, int units, int defendUnit) throws IllegalStateException, CountryNotFoundException, NotANeighbourCountry {
        riskAdmin.makeAttack(from, to, player, units, defendUnit);
    }
    public boolean validateOwnership(String country, Player attacker) {
        return riskAdmin.validateOwnership(country, attacker);

    }
    public boolean validateAttackUnits(int numberOfUnits){
        return riskAdmin.validateAttackUnits(numberOfUnits);
    }
    public boolean validateRestOfUnitWhenAttack(Country from, int numberOfUnits){
        return riskAdmin.validateRestOfUnitWhenAttack(from, numberOfUnits);
    }
    public boolean validateNeighboring(Country attackerCountry, Country defenderCountry) throws NotANeighbourCountry {
        return riskAdmin.validateNeighboring(attackerCountry, defenderCountry);
    }

    public String getCountryInformation(String countryName) throws CountryNotFoundException {
        return (riskAdmin.getCountryInformation(countryName));
    }

    public String moveUnits(Player player, Country fromCountry, Country toCountry, int units){
        return (riskAdmin.moveUnits(player, fromCountry, toCountry, units));}


    public String getAvailableColors() {
        return riskAdmin.getAvailableColors();
    }

    public void validateAttackConditions(Country from, Country to, Player player, int units, int defenderUnits) throws NotANeighbourCountry {
        riskAdmin.validateAttackConditions(from, to, player, units, defenderUnits);
    }

    public String continentPlayerCheck(Player playerOnTurn) {
        return riskAdmin.continentPlayerCheck(playerOnTurn);
    }
}
