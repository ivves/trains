package ivves.trains;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Trains {

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
        return 0;
    }
}
