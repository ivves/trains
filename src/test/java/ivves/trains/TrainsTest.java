package ivves.trains;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrainsTest {

    private final Trains trains = new Trains("AB5", "BC4", "CD8", "DC8", "DE6", "AD5", "CE2", "EB3", "AE7");

    @Test
    public void testDistanceOk() {
        assertEquals(9, trains.distance('A', 'B', 'C'));
        assertEquals(5, trains.distance('A', 'D'));
        assertEquals(13, trains.distance('A', 'D', 'C'));
        assertEquals(22, trains.distance('A', 'E', 'B', 'C', 'D'));
    }

    @Test(expected = NoSuchRouteException.class)
    public void testDistanceNoSuchRoute() {
        trains.distance('A', 'E', 'D');
    }

    @Test
    public void testCountRoutes() throws Exception {

    }

    @Test
    public void testShortestRouteDistance() throws Exception {

    }
}
