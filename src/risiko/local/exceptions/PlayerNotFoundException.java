package risiko.local.exceptions;

public class PlayerNotFoundException extends Exception {
    public PlayerNotFoundException(String playerName) {
        super("Player " + playerName + " not found");
    }
}
