package optimisationAlgorithms;

/**
 * Created by dario on 08/04/15.
 */
public class Cost {

    private final double cost;
    private final Weights weights;

    public Cost(double cost, Weights weights) {
        this.cost = cost;
        this.weights = weights;
    }

    public double getCost() {
        return cost;
    }

    public Weights getWeights() {
        return weights;
    }
}
