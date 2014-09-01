package ivves.trains;

import java.util.*;
import java.util.function.Predicate;
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

    public int distance(String route) throws NoSuchRouteException {
        int result = 0;
        City lastCity = null;
        for (char cityName : route.toCharArray()) {
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
        Map<Integer, Set<String>> routesByMetric = generateRoutesByMetric(initialCity, condition);
        Set<String> routesToCount = selectRoutesByOperator(routesByMetric, condition);
        return (int) routesToCount.stream()
                .filter(route -> route.endsWith(targetCity.getName()))
                .count();
    }

    private Map<Integer, Set<String>> generateRoutesByMetric(City initialCity, RouteCondition condition) {
        switch (condition.metric) {
            case STOPS:
                return generateRoutesByStops(initialCity, condition);
            case DISTANCE:
                return generateRoutesByDistance(initialCity, condition);
            default:
                throw new IllegalArgumentException("Unsupported condition metric: " + condition.metric);
        }
    }

    private Map<Integer, Set<String>> generateRoutesByStops(City initialCity, RouteCondition condition) {
        Map<Integer, Set<String>> routesByStops = new HashMap<>();
        Set<String> initialRoutes = initialCity.neighbors().stream()
                .map(c -> initialCity.getName() + c.getName())
                .collect(Collectors.toSet());
        routesByStops.put(1, initialRoutes);
        for (int i = 2; i <= condition.getBoundary(); i++) {
            Set<String> lastRoutes = routesByStops.get(i - 1);
            Set<String> currentRoutes = lastRoutes.stream()
                    .flatMap(r -> lastCity(r).neighbors().stream()
                            .map(n -> r + n.getName()))
                    .collect(Collectors.toSet());
            routesByStops.put(i, currentRoutes);
        }
        return routesByStops;
    }

    private Map<Integer, Set<String>> generateRoutesByDistance(City initialCity, RouteCondition condition) {
        Map<Integer, Set<String>> routesByStops = new HashMap<>();
        Predicate<String> shorterThanBoundary = route -> distance(route) < condition.getBoundary();
        Set<String> initialRoutes = initialCity.neighbors().stream()
                .map(c -> initialCity.getName() + c.getName())
                .filter(shorterThanBoundary)
                .collect(Collectors.toSet());
        routesByStops.put(1, initialRoutes);
        int i = 1;
        while (!routesByStops.get(i).isEmpty()) {
            i++;
            Set<String> lastRoutes = routesByStops.get(i - 1);
            Set<String> currentRoutes = lastRoutes.stream()
                    .flatMap(r -> lastCity(r).neighbors().stream()
                            .map(n -> r + n.getName()))
                    .filter(shorterThanBoundary)
                    .collect(Collectors.toSet());
            routesByStops.put(i, currentRoutes);
        }
        return routesByStops;
    }

    private City lastCity(String route) {
        return cityByName.get(route.charAt(route.length()-1));
    }

    private Set<String> selectRoutesByOperator(Map<Integer, Set<String>> routesByMetric, RouteCondition condition) {
        Set<String> routesToCount = new HashSet<>();
        switch (condition.operator) {
            case LESS_THAN:
                routesByMetric.forEach((stops, routes) -> routesToCount.addAll(routes));
                break;
            case EQUALS:
                routesToCount.addAll(routesByMetric.get(condition.getBoundary()));
        }
        return routesToCount;
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
