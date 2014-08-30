package ivves.trains;

public class RouteCondition {

    enum Metric {STOPS, DISTANCE}

    enum Operator {LESS_THAN, EQUALS}

    private final Metric metric;
    private final Operator operator;
    private final int value;

    public static RouteCondition stops(Operator operator, int value) {
        return new RouteCondition(Metric.STOPS, operator, value);
    }

    public static RouteCondition distance(Operator operator, int value) {
        return new RouteCondition(Metric.DISTANCE, operator, value);
    }

    public RouteCondition(Metric metric, Operator operator, int value) {
        this.metric = metric;
        this.operator = operator;
        this.value = value;
    }
}
