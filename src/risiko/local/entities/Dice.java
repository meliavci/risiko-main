package risiko.local.entities;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

public class Dice {
    private Random random;
    private Scanner scanner;

    public Dice() {
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }
    /*
     * @autor: Yvann
     * @param: numDice
     * Methode zum rollen der Würfel und vergleich der Würfelergebnisse
     * */
    public Integer[] rollDice(int numDice) {
        Integer[] results = new Integer[numDice];
        Random random = new Random();

        for (int i = 0; i < numDice; i++) {
            results[i] = random.nextInt(6) + 1;
        }

        Arrays.sort(results, Collections.reverseOrder());
        return results;

    }
    public int[] promptDiceRolls(String message,int numberOfRolls) {
        int[] rolls = new int[numberOfRolls];
        System.out.println(message);
        String input = scanner.nextLine();
        String [] inputArray = input.split(" ");
        for (int i = 0; i < numberOfRolls; i++) {
            int roll =  Integer.parseInt(inputArray[i]);
            if (roll < 1 || roll > 6) {
                throw new IllegalArgumentException("Invalid roll: " + roll + ". Must be between 1 and 6.");
            } else {
                rolls[i] = roll;
            }
        }
        Arrays.sort(rolls);
        return rolls;
    }

    private int randomNumber() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
}