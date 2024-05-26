package risiko.local.exceptions;

public class NotANeighbourCountry extends Exception {
    public NotANeighbourCountry(String countryName) {
        super("Country " + countryName + " is not a neighbour country. Please enter a valid neighbour country.");
    }
}
