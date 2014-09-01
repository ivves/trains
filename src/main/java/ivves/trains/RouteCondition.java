package ivves.trains;

public class RouteCondition {

    enum Metric {STOPS, DISTANCE}

    enum Operator {LESS_THAN, EQUALS}

    public final Metric metric;
    public final Operator operator;
    private final int value;

    public static RouteCondition stops(Operator operator, int value) {
        return new RouteCondition(Metric.STOPS, operator, value);
    }

    public static RouteCondition distance(int value) {
        return new RouteCondition(Metric.DISTANCE, Operator.LESS_THAN, value);
    }

    private RouteCondition(Metric metric, Operator operator, int value) {
        this.metric = metric;
        this.operator = operator;
        this.value = value;
    }

    public int getBoundary() {
        if (operator == Operator.LESS_THAN)
            return value - 1;
        return value;
    }
}
