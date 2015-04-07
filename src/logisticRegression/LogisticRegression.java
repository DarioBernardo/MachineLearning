package logisticRegression;

import linearAlgebra.Matrix;

/**
 * Created by dario on 22/12/14.
 */
public class LogisticRegression {


    private final Matrix theta;
    private final double threshold;

    public LogisticRegression(Matrix theta, double threshold) {
        this.theta = theta;
        this.threshold = threshold;
    }

    public LogisticRegression(Matrix theta) {
        this(theta, 0.5);
    }

    public static LogisticRegression createLogisticRegression(Matrix trainData, Matrix trainOutput, double alpha, int numberOfIterations, double lambda) {
        GradientDescent algorithm = new GradientDescent(alpha, trainData, trainOutput);
        final Matrix finalTheta = algorithm.performAlgorithm(numberOfIterations, lambda);

        return new LogisticRegression(finalTheta);
    }

    public boolean[] predictABounce(Matrix matrix) {

        Matrix result = (matrix.multiply(this.theta)).sigmoid();
        int numberOfRows = result.getNumberOfRows();
        boolean[] predictions = new boolean[numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            final double prediction = result.get(i, 0);
            predictions[i] = (prediction >= threshold);
        }
        return predictions;
    }

    public static Matrix hypothesis(Matrix x, Matrix theta) {

        return x.multiply(theta).sigmoid();
    }


    public static double regularisationFactor(Matrix theta, double lambda, double dataSetSize) {

        double sum = theta.removeFirstRow().squared().sum();
        return lambda / (2 * dataSetSize) * sum;
    }

}
