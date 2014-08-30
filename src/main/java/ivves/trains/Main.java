package ivves.trains;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.function.Supplier;

import static ivves.trains.RouteCondition.Operator.EQUALS;
import static ivves.trains.RouteCondition.Operator.LESS_THAN;
import static ivves.trains.RouteCondition.distance;
import static ivves.trains.RouteCondition.stops;
import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;

public class Main {

    private final Trains trains;
    private int outputCounter = 0;

    public static void main(String... args) {
        Main main = new Main(args);
        main.calculationTasks().forEach(main::output);
    }

    public Main(String... args) {
        trains = new Trains(args);
    }

    List<Supplier<Integer>> calculationTasks() {
        return asList(
                () -> trains.distance('A', 'B', 'C'),
                () -> trains.distance('A', 'D'),
                () -> trains.distance('A', 'D', 'C'),
                () -> trains.distance('A', 'E', 'B', 'C', 'D'),
                () -> trains.distance('A', 'E', 'D'),
                () -> trains.countRoutes('C', 'C', stops(LESS_THAN, 4)),
                () -> trains.countRoutes('A', 'C', stops(EQUALS, 4)),
                () -> trains.shortestRouteDistance('A', 'C'),
                () -> trains.shortestRouteDistance('B', 'B'),
                () -> trains.countRoutes('C', 'C', distance(LESS_THAN, 30)));
    }

    private void output(Supplier<Integer> resultSupplier) {
        outputCounter++;
        System.out.println(format("Output #{0}: {1}", outputCounter, getResult(resultSupplier)));
    }

    private String getResult(Supplier<Integer> resultSupplier) {
        try {
            Integer result = resultSupplier.get();
            if (result == null)
                throw new IllegalArgumentException("Calculation result cannot be null");
            return result.toString();
        } catch (NoSuchRouteException e) {
            return "NO SUCH ROUTE";
        }
    }
}
