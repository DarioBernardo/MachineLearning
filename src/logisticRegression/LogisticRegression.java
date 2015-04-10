package logisticRegression;

import linearAlgebra.Matrix;
import optimisationAlgorithms.GenericGradientDescent;
import optimisationAlgorithms.Predictor;

/**
 * Created by dario on 22/12/14.
 */
public class LogisticRegression extends Predictor {


    private final Matrix theta;
    private final double threshold;

    public LogisticRegression(LogisticRegressionWeights theta, double threshold) {
        this.theta = theta.getThetas();
        this.threshold = threshold;
    }

    public LogisticRegression(LogisticRegressionWeights theta) {
        this(theta, 0.5);
    }

    public static LogisticRegression createLogisticRegression(Matrix trainData, Matrix trainOutput, double alpha, int numberOfIterations, double lambda) {


        LogisticRegressionWeights initialTheta = new LogisticRegressionWeights(buildInitialTheta(trainData.getNumberOfColumns()));
        GenericGradientDescent<LogisticRegressionCostCalculator, LogisticRegressionWeights> algorithm = new GenericGradientDescent<>(alpha, trainData, trainOutput, initialTheta);
        final LogisticRegressionWeights finalWeights = algorithm.performAlgorithm(new LogisticRegressionCostCalculator(), numberOfIterations, lambda);

        return new LogisticRegression(finalWeights);
    }

    public boolean[] predictABounce(Matrix matrix) {

        Matrix result = hypothesis(matrix);
        int numberOfRows = result.getNumberOfRows();
        boolean[] predictions = new boolean[numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            final double prediction = result.get(i, 0);
            predictions[i] = (prediction >= threshold);
        }
        return predictions;
    }

    public Matrix hypothesis(Matrix x) {

        return x.multiply(theta).sigmoid();
    }


    public static double regularisationFactor(Matrix theta, double lambda, double dataSetSize) {

        double sum = theta.removeFirstRow().squared().sum();
        return lambda / (2 * dataSetSize) * sum;
    }

    public static Matrix buildInitialTheta(int numberOfColumns) {

        double[][] zeros = new double[numberOfColumns][1];
        for (int i = 0; i < numberOfColumns; i++) {
            zeros[i][0] = 0;
        }

        return new Matrix(zeros);
    }

}
