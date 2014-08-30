package ivves.trains;

import java.util.*;
import java.util.stream.Collectors;

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

    public int countRoutes(char start, char finish, RouteCondition condition) {
        if (condition == null)
            throw new IllegalArgumentException("Cannot count routes without condition");
        City initialCity = cityByName.get(start);
        City targetCity = cityByName.get(finish);
        switch (condition.metric) {
            case STOPS:
                return countRoutesByStops(initialCity, targetCity, condition);
            case DISTANCE:
                return countRoutesByDistance(initialCity, targetCity, condition);
            default:
                throw new IllegalArgumentException("Unsupported condition metric: " + condition.metric);
        }
    }

    private int countRoutesByStops(City initialCity, City targetCity, RouteCondition condition) {
        Map<Integer, Set<String>> routesByStops = generateRoutesByStops(initialCity, condition);
        Set<String> routesToCount = selectRoutesToCount(routesByStops, condition);
        return (int) routesToCount.stream()
                .filter(route -> route.endsWith(targetCity.getName()))
                .count();
    }

    private Map<Integer, Set<String>> generateRoutesByStops(City initialCity, RouteCondition condition) {
        Map<Integer, Set<String>> routesByStops = new HashMap<>();
        Set<String> initialRoutes = initialCity.neighbors().stream()
                .map(c -> initialCity.getName() + c.getName())
                .collect(Collectors.toSet());
        routesByStops.put(1, initialRoutes);
        for (int i = 2; i <= condition.getBoundary(); i++) {
            Set<String> lastRoutes = routesByStops.get(i - 1);
            Set<String> currentRoutes = new HashSet<>();
            for (String route : lastRoutes) {
                for (City neighbor : lastCity(route).neighbors()) {
                    currentRoutes.add(route + neighbor.getName());
                }
            }
            routesByStops.put(i, currentRoutes);
        }
        return routesByStops;
    }

    private Set<String> selectRoutesToCount(Map<Integer, Set<String>> routesByStops, RouteCondition condition) {
        Set<String> routesToCount = new HashSet<>();
        switch (condition.operator) {
            case LESS_THAN:
                routesByStops.forEach((stops, routes) -> routesToCount.addAll(routes));
                break;
            case EQUALS:
                routesToCount.addAll(routesByStops.get(condition.getBoundary()));
        }
        return routesToCount;
    }

    private City lastCity(String route) {
        return cityByName.get(route.charAt(route.length()-1));
    }

    private int countRoutesByDistance(City initialCity, City targetCity, RouteCondition condition) {
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
