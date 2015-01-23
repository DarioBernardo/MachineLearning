package logisticRegression;

import linearAlgebra.Matrix;

/**
 * Created by dario on 13/01/15.
 */
public class GradientDescent {


    private final double alpha;
    private final Matrix trainingData;
    private final Matrix targetValues;

    public GradientDescent(double alpha, Matrix trainingData, Matrix targetValues) {
        this.alpha = alpha;
        this.trainingData = trainingData;
        this.targetValues = targetValues;
    }

    public Matrix performAlgorithm(int numberOfIterations, double lambda) {


        Matrix finalThetas = buildInitialTheta();
        for (int iteration = 0; iteration < numberOfIterations; iteration++) {

            LogisticRegressionCost logisticRegressionCost = LogisticRegressionCost.create(finalThetas, trainingData, targetValues, lambda);

            double cost = logisticRegressionCost.getCost();


            if (iteration % (numberOfIterations / 10) == 0 /*|| cnt % 1_000_000L == 0*/) {
                double progress = iteration * 100.0 / (1.0 * numberOfIterations);
                System.out.println("Cost at iteration " + iteration + " is: " + cost + "  (" + Math.round(progress) + "%)");
            }

            Matrix gradient = logisticRegressionCost.getGradient();

            finalThetas = finalThetas.subtract((gradient.transpose().multiply(alpha)));
        }

        return finalThetas;
    }


    public Matrix buildInitialTheta() {

        int size = trainingData.getNumberOfColumns();

        double[][] zeros = new double[size][1];
        for (int i = 0; i < size; i++) {
            zeros[i][0] = 0;
        }

        return new Matrix(zeros);
    }
}
