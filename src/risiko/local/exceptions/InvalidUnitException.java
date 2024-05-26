package risiko.local.exceptions;

public class InvalidUnitException extends Exception {
    public InvalidUnitException(String input) {
        super("The input: " + input + " is not a valid unit. Please enter a valid unit.");
    }
}
