package neuralNetwork;

import linearAlgebra.Matrix;
import logisticRegression.LogisticRegressionCostCalculator;
import logisticRegression.LogisticRegressionWeights;
import optimisationAlgorithms.Cost;
import optimisationAlgorithms.CostCalculator;
import optimisationAlgorithms.Weights;

/**
 * Created by dario on 09/04/15.
 */
public class NeuralNetworkCostCalculator implements CostCalculator<NNWeights> {

    private final int outClassesNumber;

    public NeuralNetworkCostCalculator(int outClassesNumber) {
        this.outClassesNumber = outClassesNumber;
    }

    @Override
    public Cost calculate(NNWeights theta, Matrix x, Matrix y, double lambda) {


        Matrix expandedOut;
        if(outClassesNumber > 1) {
            expandedOut = NeuralNetwork.expandOutput(y, outClassesNumber);
        }else {
            expandedOut = y;
        }

        double cost = NeuralNetwork.computeCost(x, expandedOut, theta, lambda);
        NNWeights weights = BackPropagation.performAlgorithm(theta, x, expandedOut, lambda);

        return new Cost(cost, weights);
    }
}
