package ivves.trains;

import java.util.HashMap;
import java.util.Map;

public final class City {
    private final char name;
    private Map<City, Integer> distancesToNeighbors = new HashMap<>();

    public City(char name) {
        this.name = name;
    }

    public void addRoute(City neighbor, int distance) {
        distancesToNeighbors.put(neighbor, distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (name != city.name) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) name;
    }

    public int distanceTo(City neighbor) {
        if (distancesToNeighbors.containsKey(neighbor))
            return distancesToNeighbors.get(neighbor);
        throw new NoSuchRouteException();
    }
}
