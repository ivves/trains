package ivves.trains;

import org.junit.Test;

import static ivves.trains.RouteCondition.Operator.*;
import static ivves.trains.RouteCondition.distance;
import static ivves.trains.RouteCondition.stops;
import static org.junit.Assert.assertEquals;

public class TrainsTest {

    private final Trains trains = new Trains("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7");

    @Test
    public void testDistanceOk() {
        assertEquals(9, trains.distance("ABC"));
        assertEquals(5, trains.distance("AD"));
        assertEquals(13, trains.distance("ADC"));
        assertEquals(22, trains.distance("AEBCD"));
    }

    @Test(expected = NoSuchRouteException.class)
    public void testDistanceNoSuchRoute() {
        trains.distance("AED");
    }

    @Test
    public void testCountRoutesByStops() throws Exception {
        assertEquals(2, trains.countRoutes('C', 'C', stops(LESS_THAN, 4)));
        assertEquals(3, trains.countRoutes('A', 'C', stops(EQUALS, 4)));
        assertEquals(0, trains.countRoutes('B', 'A', stops(LESS_THAN, 10)));
    }

    @Test
    public void testCountRoutesByDistance() throws Exception {
        assertEquals(7, trains.countRoutes('C', 'C', distance(30)));
        assertEquals(0, trains.countRoutes('B', 'A', distance(100)));
    }

    @Test
    public void testShortestRouteDistance() throws Exception {
        assertEquals(9, trains.shortestRouteDistance('A', 'C'));
        assertEquals(9, trains.shortestRouteDistance('B', 'B'));
    }
}
