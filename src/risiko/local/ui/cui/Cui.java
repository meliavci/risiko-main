package risiko.local.ui.cui;
import risiko.local.domain.*;
import risiko.local.entities.*;
import risiko.local.exceptions.CountryNotFoundException;
import risiko.local.exceptions.InvalidUnitException;
import risiko.local.exceptions.NotANeighbourCountry;
import risiko.local.exceptions.PlayerNotFoundException;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.String;

public class Cui {
    private List<Player> players;

    private Risk risk;


    private BufferedReader input;
    private int playerId = 0;

    public Cui(String dataName){
        this.players = new ArrayList<>();
        this.risk = new Risk(dataName);
        this.input = new BufferedReader(new InputStreamReader(System.in));

    }

    private String readInput(){
        try {
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }

    /**
     * Starts the game by assigning countries to players
     * The players will be asked to choose a country in turns
     */


    public void startGame() throws CountryNotFoundException, InvalidUnitException {

        System.out.println(risk.pickRandomStartCountries());
        for (Player player : players){
            System.out.println("Player " + player.getName() + " has the following countries: ");
            System.out.println(player.getCountryNames());
        }
        gameMenu();
    }


    /**
     * The game menu where the player can choose what to do
     * The player can attack, move units, end turn or exit the game
     */
    private void gameMenu() throws CountryNotFoundException, InvalidUnitException {
        String option;
        String input;
        int units;

        risk.nextTurn();
        System.out.println("Player " +risk.getPlayerOnTurn().getName() +" is on turn");
        System.out.println(risk.continentPlayerCheck(risk.getPlayerOnTurn()));
        System.out.println(risk.addPlayerUnitsPerRound(risk.getPlayerOnTurn()));
        while (risk.getPlayerOnTurn().getNumberOfUnits() > 0) {
            System.out.println("Please pick a Country:");
            System.out.println(risk.getPlayerOnTurn().getCountryNames());
            input = readInput();
            System.out.println("How many Units do you want to place?");
            while (true) {
                try {
                    units = Integer.parseInt(readInput());
                    if (units > risk.getPlayerOnTurn().getNumberOfUnits()) {
                        System.out.println("You don't have enough units. You have " + risk.getPlayerOnTurn().getNumberOfUnits() + " units available.");
                        continue;
                    }
                    break; // will only reach this line if the input was a valid integer and the player has enough units, so we break the loop
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
            System.out.println(risk.addPlayerUnitsToCountry(risk.getPlayerOnTurn(), input, units));
        }

        do {

            System.out.println("Please choose an option:");
            System.out.println("1. Attack Country");
            System.out.println("2. Move Units");
            System.out.println("3. Get Country Information");
            System.out.println("4. End Turn");
            System.out.println("5. Exit");

            option = readInput();
            switch (option) {
                case "4":
                    risk.nextTurn();
                    System.out.println("Player " +risk.getPlayerOnTurn().getName() +" is on turn");
                    System.out.println(risk.continentPlayerCheck(risk.getPlayerOnTurn()));
                    System.out.println(risk.addPlayerUnitsPerRound(risk.getPlayerOnTurn()));
                    while (risk.getPlayerOnTurn().getNumberOfUnits() > 0) {
                        System.out.println("Please pick a Country:");
                        System.out.println(risk.getPlayerOnTurn().getCountryNames());
                        input = readInput();
                        System.out.println("How many Units do you want to place?");
                        while (true) {
                            try {
                                units = Integer.parseInt(readInput());
                                if (units > risk.getPlayerOnTurn().getNumberOfUnits()) {
                                    System.out.println("You don't have enough units. You have " + risk.getPlayerOnTurn().getNumberOfUnits() + " units available.");
                                    continue;
                                }
                                break; // will only reach this line if the input was a valid integer and the player has enough units, so we break the loop
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a valid number.");
                            }
                        }
                        System.out.println(risk.addPlayerUnitsToCountry(risk.getPlayerOnTurn(), input, units));
                    }
                    break;
                case "1":
                    try {
                        makeAttack();
                    } catch (IllegalStateException | NumberFormatException e) {
                        System.out.println("Attention: " + e.getMessage());
                    } catch (NotANeighbourCountry e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "5":
                    System.exit(0);
                    break;
                case "2":
                    boolean movingUnits = true;
                    System.out.println("Do you want to move units? [y/n]");
                    String answer = readInput();
                    while (movingUnits = true) {
                        while (!answer.equals("y") && !answer.equals("n")) {
                            System.out.println("Invalid input. Please enter y or n.");
                            answer = readInput();
                        }
                        if (answer.equals("n")) {
                            movingUnits = false;
                            break;
                        }
                        try {
                            System.out.println("Enter the name of the country from where you want to move units: ");
                            String fromCountryName = readInput();
                            while (!risk.validateOwnership(fromCountryName, risk.getPlayerOnTurn())) {
                                System.out.println("Give a country that you own :");
                                fromCountryName = readInput();
                            }
                            Country fromCountry = risk.getCountryByName(fromCountryName);
                            System.out.println("The neighbors of " + fromCountryName + " are: ");
                            for (Country neighbor : fromCountry.getNeighbors()) {
                                System.out.println(neighbor.getName());
                            }

                            System.out.println("Enter the name of the neighbor country to where you want to move units: ");
                            // check for if neigbour country is valid
                            String toCountryName = readInput();
                            Country toCountry = risk.getCountryByName(toCountryName);

                            // check if number of units is valid
                            System.out.println("The number of units in " + fromCountryName + " is: " + fromCountry.getUnits());
                            System.out.println("Enter the number of units you want to move: ");
                            while (true) {
                                try {
                                    units = Integer.parseInt(readInput());
                                    if (units > fromCountry.getUnits() - 1) {
                                        System.out.println("You don't have enough units. You have " + (fromCountry.getUnits() - 1) + " units available.");
                                        continue;
                                    }
                                    break; // will only reach this line if the input was a valid integer and the player has enough units, so we break the loop
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                }
                            }

                            System.out.println(risk.moveUnits(risk.getPlayerOnTurn(), fromCountry, toCountry, units));
                        } catch (CountryNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
//                        } catch (InvalidUnitException e) {
//                            System.out.println("Error: " + e.getMessage());
//                        }
                        System.out.println("Do you want to move units again? [y/n]");
                        answer = readInput();
                        while (!answer.equals("y") && !answer.equals("n")) {
                            System.out.println("Invalid input. Please enter y or n.");
                            answer = readInput();
                        }
                        if (answer.equals("n")) {
                            movingUnits = false;
                            break;
                        }
                    }
                    break;
                case "3":
                    System.out.println(risk.listAllCountries());
                    System.out.println("Enter the name of the country you want to get information about: ");
                    String countryName = readInput();
                    System.out.println(risk.getCountryInformation(countryName));
                    break;
                default:
                    System.out.println("Invalid option");
                    gameMenu();
            }
        } while (option !="4");
    }

    /**
     * The main menu where the player can add or remove players, list all players, start the game or exit
     */
    public void mainMenu() throws CountryNotFoundException, InvalidUnitException {
        String playerName;
        int color;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Risiko!");
        String option;
        do {
            System.out.println("Please choose an option:");
            System.out.println("a = Add Player");
            System.out.println("r = Remove Player");
            System.out.println("l = List all Players");
            System.out.println("s = Start Game");
            System.out.println("q = Exit");

            option = readInput();
            switch (option) {
                case "a":
                    System.out.print("Enter the player name: \n>");
                    playerName = readInput();
                    String colorName;
                    do {
                        System.out.println("Available colors: " + risk.getAvailableColors());
                        System.out.print("Enter player color: ");
                        colorName = readInput();
                        if (!risk.getAvailableColors().contains(colorName)){
                            System.out.println("Color " + colorName + " has already been selected by another player or is invalid. Please choose a different color.");
                        }
                    } while (!risk.getAvailableColors().contains(colorName));

                    players.add(risk.addPlayer(playerName, playerId, colorName));
                    playerId++;
                    break;

                case "r":
                    System.out.println("Current Players are: " + risk.listPlayers());
                    System.out.print("Enter player name: \n>");
                    playerName = readInput();
                    try {
                        risk.removePlayer(playerName);
                    } catch (PlayerNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "l":
                    System.out.println(risk.listPlayers());
                    break;
                case "s":
                    if (players.size() < 2) {
                    System.out.println("You need at least 2 players to start the game");
                    break;
                }
                    startGame();
                    break;

                case "q":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
                    mainMenu();
            }
        } while (option !="q");
    }

    /**
     * @author Melisa
     * The attack method where the player can choose from which country to attack and to which country
     * The player can choose how many units to attack with and how many units to defend with
     */
    public void makeAttack() throws CountryNotFoundException, NotANeighbourCountry {
        boolean attackLoop = true;
        System.out.println(risk.getPlayerOnTurn().getName() + " do you want to attack a country? [y/n]");
        String answer = readInput();
        while (!answer.equals("y") && !answer.equals("n")) {
            System.out.println("Invalid input. Please enter y or n.");
            answer = readInput();
        } if (answer.equals("n")) {
            attackLoop = false;
        }
        while (attackLoop) {
            String fromCountry = "";
            String currentNeighbor;
            System.out.println("From which country do you want to attack?");
            Boolean hasAttackCountries = null;
            for (int i = 0; i < risk.getPlayerOnTurn().getCountries().size(); i++) {
                Country currentCountry = risk.getPlayerOnTurn().getCountries().get(i);
                if (currentCountry.getUnits() > 1) {
                    System.out.println(currentCountry);
                    hasAttackCountries = true;
                }
            }
            if (!Boolean.TRUE.equals(hasAttackCountries)) {
                System.out.println("You do not have any countries to attack from.");
                attackLoop = false;
                return;
            }
            boolean validCountry = false;
            while (!validCountry) {
                try {
                    fromCountry = readInput();
                    while (risk.getCountryByName(fromCountry).getUnits() < 2) {
                        System.out.println("You need at least 2 units to attack. Give another country :");
                        fromCountry = readInput();
                    }
                    while (!risk.validateOwnership(fromCountry, risk.getPlayerOnTurn())) {
                        System.out.println("Give a country that you own :");
                        fromCountry = readInput();
                    }
                    validCountry = true; // If we reach this point, the country is valid and we can exit the loop
                } catch (CountryNotFoundException e) {
                    System.out.println("Country not found. Please enter a valid country name.");
                }
            }
            int units;
            if (risk.getCountryByName(fromCountry).getUnits() - 1 < 3) {
                units = risk.getCountryByName(fromCountry).getUnits() - 1;
            } else {
                units = 3;
            }
            System.out.println("You can place 1-" + units + " units to attack. How many units do you want to place?");
            int units2 = Integer.parseInt(readInput());
            while (!risk.validateAttackUnits(units2)) {
                System.out.println("Your input was invalid. Give another number [1-" + units + "]:");
                units2 = Integer.parseInt(readInput());
            }
            String toCountry = "";
            System.out.println("Which country do you want to attack?");
            for (int i = 0; i < risk.getCountryByName(fromCountry).getNeighbors().size(); i++) {
                currentNeighbor = risk.getCountryByName(fromCountry).getNeighbors().get(i).getName();
                if (!risk.validateOwnership(currentNeighbor, risk.getPlayerOnTurn())) {
                    System.out.println(currentNeighbor);
                }
            }
            boolean validCountry2 = false;
            Country from = null;
            Country to = null;
            while (!validCountry2) {
                try {
                    toCountry = readInput();
                    risk.getCountryByName(toCountry); // This will throw CountryNotFoundException if the country does not exist
                    while (risk.validateOwnership(toCountry, risk.getPlayerOnTurn())) {
                        System.out.println("Give a country that you don't own :");
                        toCountry = readInput();
                    }
                    validCountry2 = true; // If we reach this point, the country is valid and we can exit the loop
                } catch (CountryNotFoundException e) {
                    System.out.println("Country not found. Please enter a valid country name.");
                }
                to = risk.getCountryByName(toCountry);
                from = risk.getCountryByName(fromCountry);
                boolean areNeighbours = false;
                while (!areNeighbours) {
                    try {
                        if (!risk.validateNeighboring(from, to)) {
                            throw new NotANeighbourCountry(toCountry);
                        }
                        areNeighbours = true; // If we reach this point, the countries are neighbours and we can exit the loop
                    } catch (NotANeighbourCountry e) {
                        System.out.println("Error: " + e.getMessage());
                        toCountry = readInput();
                        to = risk.getCountryByName(toCountry);
                    }
                }
            }
            System.out.println("From: " + from.getName() + " To: " + to.getName() + " Units: " + units);
            Player player = risk.getPlayerOnTurn();
            if (to.getUnits() > 1) {
                System.out.println(to.getPlayer().getName() + " how many units to you want to place to defend " + to.getName() + " [1 - 2]?");
            } else {
                System.out.println(to.getPlayer().getName() + " how many units to you want to place to defend " + to.getName() + " [1]?");
            }
            int defenderUnits = 0;
            while (defenderUnits > 2 || defenderUnits < 1) {
                defenderUnits = Integer.parseInt(readInput());
                System.out.println("You can only place 1 or 2 units to defend.");
                System.out.println("Please enter a valid number of units to defend [1 - 2]:");
            }
            risk.makeAttack(from, to, player, units, defenderUnits);
            System.out.println("Do you want to attack again? [y/n]");
            answer = readInput();
            while (!answer.equals("y") && !answer.equals("n")) {
                System.out.println("Invalid input. Please enter y or n.");
                answer = readInput();
            }
            if (answer.equals("n")) {
                attackLoop = false;
                break;
            }
        }

    }

    public static void main (String[] args) throws CountryNotFoundException, InvalidUnitException {
        Cui cui = new Cui("Risk");
        cui.mainMenu();
    }
}
