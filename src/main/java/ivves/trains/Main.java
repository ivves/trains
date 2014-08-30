package ivves.trains;

import java.util.List;
import java.util.function.Supplier;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;

public class Main {

    private final Trains trains;
    private int outputCounter = 0;

    public static void main(String... args) {
        Main main = new Main(args);
        main.outputResults();
    }

    public Main(String... args) {
        trains = new Trains(args);
    }

    private void outputResults() {
        List<Supplier<Integer>> calculations = asList(
                () -> trains.distance("ABC"),
                () -> trains.distance("AD"),
                () -> trains.distance("ADC"),
                () -> trains.distance("AEBCD"),
                () -> trains.distance("AED"),
                () -> trains.countRoutes("C", "C", "stops < 4"),
                () -> trains.countRoutes("A", "C", "stops = 4"),
                () -> trains.shortestRouteDistance("A", "C"),
                () -> trains.shortestRouteDistance("B", "B"),
                () -> trains.countRoutes("C", "C", "distance < 30"));
        calculations.forEach(this::output);
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
