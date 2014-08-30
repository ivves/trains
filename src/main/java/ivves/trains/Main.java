package ivves.trains;

import java.util.function.Supplier;

import static java.text.MessageFormat.format;

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
        output(() -> trains.distance("ABC"));
        output(() -> trains.distance("AD"));
        output(() -> trains.distance("ADC"));
        output(() -> trains.distance("AEBCD"));
        output(() -> trains.distance("AED"));
        output(() -> trains.countRoutes("C", "C", "stops < 4"));
        output(() -> trains.countRoutes("A", "C", "stops = 4"));
        output(() -> trains.shortestRouteDistance("A", "C"));
        output(() -> trains.shortestRouteDistance("B", "B"));
        output(() -> trains.countRoutes("C", "C", "distance < 30"));
    }

    private void output(Supplier<Integer> msgSupplier) {
        outputCounter++;
        System.out.println(format("Output #{0}: {1}", outputCounter, getMessage(msgSupplier)));
    }

    private String getMessage(Supplier<Integer> msgSupplier) {
        try {
            return msgSupplier.get().toString();
        } catch (NoSuchRouteException e) {
            return "NO SUCH ROUTE";
        }
    }
}
