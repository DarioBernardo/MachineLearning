package optimisationAlgorithms;

import linearAlgebra.Matrix;

/**
 * Created by dario on 08/04/15.
 */
public class GenericGradientDescent<T extends CostCalculator, J extends Weights> {

    private final double alpha;
    private final Matrix trainingData;
    private final Matrix targetValues;
    private final J initialTheta;

    public GenericGradientDescent(double alpha, Matrix trainingData, Matrix targetValues, J initialTheta) {
        this.alpha = alpha;
        this.trainingData = trainingData;
        this.targetValues = targetValues;
        this.initialTheta = initialTheta;
    }

    public J performAlgorithm(T costCalculator, int numberOfIterations, double lambda) {

        J thetas = initialTheta;
        for (int iteration = 0; iteration < numberOfIterations; iteration++) {

            Cost costClass = costCalculator.calculate(thetas, trainingData, targetValues, lambda);

            double cost = costClass.getCost();


            if (iteration % (numberOfIterations / 10) == 0 /*|| cnt % 1_000_000L == 0*/) {
                double progress = iteration * 100.0 / (1.0 * numberOfIterations);
                System.out.println("Cost at iteration " + iteration + " is: " + cost + "  (" + Math.round(progress) + "%)");
            }

            Weights gradient = costClass.getWeights();

            thetas.updateWeights(gradient, alpha);
        }

        return thetas;
    }
}
