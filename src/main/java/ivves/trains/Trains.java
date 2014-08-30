package ivves.trains;

import java.util.*;

public class Trains {

    private static final int INFINITY = Integer.MAX_VALUE;

    private final Map<Character, City> cityByName = new HashMap<>();

    public Trains(String... routes) {
        for (String route : routes) {
            if (route == null || route.length() < 3)
                throw new IllegalArgumentException("Input route must be 3 or more chars long: " + route);
            char city1Name = route.charAt(0);
            char city2Name = route.charAt(1);
            int distance = Integer.valueOf(route.substring(2));
            addRoute(city1Name, city2Name, distance);
        }
    }

    private void addRoute(char city1Name, char city2Name, int distance) {
        City city1 = getOrCreateCity(city1Name);
        City city2 = getOrCreateCity(city2Name);
        city1.addRoute(city2, distance);
    }

    private City getOrCreateCity(char cityName) {
        City city = cityByName.get(cityName);
        if (city == null) {
            city = new City(cityName);
            cityByName.put(cityName, city);
        }
        return city;
    }

    public int distance(char... cities) throws NoSuchRouteException {
        int result = 0;
        City lastCity = null;
        for (char cityName : cities) {
            City currentCity = cityByName.get(cityName);
            if (lastCity != null) {
                result += lastCity.distanceTo(currentCity);
            }
            lastCity = currentCity;
        }
        return result;
    }

    public int countRoutes(char start, char finish, String conditions) {
        return 0;
    }

    public int shortestRouteDistance(char start, char finish) {
        Map<City, Integer> distances = initDistances();
        City initialCity = cityByName.get(start);
        City targetCity = cityByName.get(finish);
        Set<City> unvisitedCities = new HashSet<>(cityByName.values());
        for (City neighbor : initialCity.neighbors()) {
            distances.put(neighbor, initialCity.distanceTo(neighbor));
        }
        while (!unvisitedCities.isEmpty() && unvisitedCities.contains(targetCity)) {
            City currentCity = chooseNextCity(unvisitedCities, distances);
            if (currentCity == null) {
                break;
            }
            unvisitedCities.remove(currentCity);
            for (City neighbor : intersection(currentCity.neighbors(), unvisitedCities)) {
                int currentDistance = distances.get(neighbor);
                int newDistance = distances.get(currentCity) + currentCity.distanceTo(neighbor);
                distances.put(neighbor, Math.min(currentDistance, newDistance));
            }
        }
        return distances.get(targetCity);
    }

    private City chooseNextCity(Set<City> unvisitedCities, Map<City, Integer> distances) {
        City nearestUnvisitedCity = null;
        int minDistance = INFINITY;
        for (City city : unvisitedCities) {
            if (distances.get(city) < minDistance) {
                minDistance = distances.get(city);
                nearestUnvisitedCity = city;
            }
        }
        return nearestUnvisitedCity;
    }

    private Map<City, Integer> initDistances() {
        Map<City, Integer> distances = new HashMap<>(cityByName.size());
        for (City city : cityByName.values()) {
            distances.put(city, INFINITY);
        }
        return distances;
    }

    private static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }
}
