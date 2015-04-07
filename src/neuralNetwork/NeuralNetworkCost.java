package neuralNetwork;

import linearAlgebra.Matrix;

/**
 * Created by dario on 04/04/15.
 */
public class NeuralNetworkCost {

    private final Matrix gradient;
    private final double cost;

    private NeuralNetworkCost(Matrix gradient, double cost) {
        this.gradient = gradient;
        this.cost = cost;
    }

    public static NeuralNetworkCost create() {

        return null;
    }

    public Matrix getGradient() {
        return gradient;
    }

    public double getCost() {
        return cost;
    }
}
