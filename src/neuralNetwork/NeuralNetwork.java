package neuralNetwork;

import linearAlgebra.Matrix;
import logisticRegression.LogisticRegressionCost;

import java.util.Random;

/**
 * Created by dario on 04/04/15.
 */
public class NeuralNetwork {

    private final Matrix theta1;
    private final Matrix theta2;

    public NeuralNetwork(Matrix theta1, Matrix theta2) {
        this.theta1 = theta1;
        this.theta2 = theta2;
    }

    public Matrix hypothesis(Matrix trainData) {

        Matrix h1 = trainData.introduceColumnOfOnesAtHead().multiply(theta1.transpose()).sigmoid();
        Matrix h0 = h1.introduceColumnOfOnesAtHead().multiply(theta2.transpose()).sigmoid();

        //System.out.println("\nh0 size:\nColumns:\t" + h0.getNumberOfColumns() + "\nRows:\t" + h0.getNumberOfRows());
        return h0;
    }

    public static double computeCost(Matrix trainData, Matrix out, Matrix theta1, Matrix theta2, double lambda) {

        NeuralNetwork nn = new NeuralNetwork(theta1, theta2);
        Matrix hypothesis = nn.hypothesis(trainData);
        Matrix expandedOut;
        if(hypothesis.getNumberOfColumns() > 1) {
            expandedOut = NeuralNetwork.expandOutput(out, hypothesis.getNumberOfColumns());
        }else {
            expandedOut = out;
        }

        double regularisationFactor = NeuralNetwork.regularisationFactor(theta1, theta2, lambda, trainData.getNumberOfRows());

        return LogisticRegressionCost.computeLogLoss(hypothesis, expandedOut, regularisationFactor);
    }

    public static double regularisationFactor(Matrix theta1, Matrix theta2, double lambda, double dataSetSize) {

        double pow = regularisationOn(theta1) + regularisationOn(theta2);

        return lambda / (2.0*dataSetSize) * pow;
    }

    private static double regularisationOn(Matrix theta1) {
        double pow = 0.0;
        for (int i = 1; i < theta1.getNumberOfColumns(); i++) {
            for (int j = 0; j < theta1.getNumberOfRows(); j++) {
                pow += Math.pow(theta1.get(j, i), 2.0);
            }
        }

        return pow;
    }

    public static Matrix expandOutput(Matrix y, int expansionSize) {

        int numberOfRows = y.getNumberOfRows();
        double[][] result = new double[numberOfRows][];
        for (int i = 0; i < numberOfRows; i++) {
            result[i] = new double[expansionSize];
            for (int j = 0; j < expansionSize; j++) {
                result[i][j] = 0;
            }
            result[i][((int) y.get(i, 0)-1)] = 1;
        }

        return new Matrix(result);
    }

    public static Matrix randInitTheta(int in, int out, long seed) {
        Random rand = new Random(seed);
        double rangeMin = -0.12;
        double rangeMax = 0.12;

        double[][] values = new double[in][out];

        for (int i = 0; i < in; i++) {
            values[i] = new double[out];
            for (int j = 0; j < out; j++) {
                values[i][j] = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
            }
        }

        return new Matrix(values);
    }

    public static void createNeuralNetwork(Matrix trainData, Matrix trainOutput, Matrix theta1, Matrix theta2) {


    }
}
