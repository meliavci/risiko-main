package risiko.local.persistence;

import risiko.local.entities.Continent;
import risiko.local.entities.Country;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

//Author: Vincent
public class CountryInitiator {
    final Map<String, List<String>> countriesAndNeighbors;

    public CountryInitiator(String filePath) {
        countriesAndNeighbors = new HashMap<>();
        loadCountriesAndNeighbors (filePath);
    }

    /**
     * Loads in all Countries and Neighbor states and puts it into countriesAndNeighbors
     * @author Vincent
     * @param filePath the path to the file with the countries and neighbors
     */
    private void loadCountriesAndNeighbors (String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String country = parts[0];
                List <String> neighbors = new ArrayList<>();
                for (int i=1; i<parts.length; i++) {
                    neighbors.add(parts[i]);
                }
                countriesAndNeighbors.put(country, neighbors);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes all Countries and their neighbors and adds all abbreviations
     * @author Vincent
     * @return a list of all initialized countries
     */
    public List<Country> initializeCountries() {
        Map<String, Country> countryMap = new HashMap<>();

        // Creates all Countries out of countriesAndNeighbors
        for (Map.Entry<String, List<String>> entry : countriesAndNeighbors.entrySet()) {
            String countryName = entry.getKey();
            String abbreviation = entry.getValue().remove(0); // Assuming abbreviation is the first element in the list
            Country country = new Country(countryName, abbreviation);
            countryMap.put(countryName, country);
        }

        // Adds Neighbors to all created Countries
        for (Map.Entry<String, List<String>> entry : countriesAndNeighbors.entrySet()) {
            String countryName = entry.getKey();
            Country country = countryMap.get(countryName);
            List<Country> neighbors = new ArrayList<>();
            for (String neighborName : entry.getValue()) {
                Country neighbor = countryMap.get(neighborName);
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
            }
            country.setNeighbors(neighbors);
        }
        return new ArrayList<>(countryMap.values());
    }

    /**
     * Initializes all Continents, their bonus units and the countries they contain
     * @author Vincent
     * @param filePath the path to the file with the continents, bonus units and their countries
     * @param countries a list of all countries to assign to the continents (should be the same as the one returned by initializeCountries)
     */
    public List<Continent> initializeContinents(String filePath, List<Country> countries) {
        List<Continent> continents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String continentName = parts[0];
                int bonusUnits = Integer.parseInt(parts[1]);
                Continent continent = new Continent(continentName, bonusUnits);
                for (int i = 2; i < parts.length; i++) {
                    String countryName = parts[i];
                    Country country = getCountryByName(countryName, countries);
                    if (country != null) {
                        continent.addCountry(country);
                    }
                }
                continents.add(continent);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return continents;
    }

    /**
     * Returns the country with the given name
     * @author Vincent
     * @param countryName the name of the country
     * @param countries a list of all countries
     * @return the country with the given name or null if no such country exists
     */
    public Country getCountryByName(String countryName, List<Country> countries) {
        for (Country country : countries) {
            if (country.getName().equals(countryName)) {
                return country;
            }
        }
        return null;
    }

}


