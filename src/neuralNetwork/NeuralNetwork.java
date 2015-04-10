package neuralNetwork;

import linearAlgebra.Matrix;
import logisticRegression.LogisticRegressionCostCalculator;
import optimisationAlgorithms.GenericGradientDescent;
import optimisationAlgorithms.Predictor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by dario on 04/04/15.
 */
public class NeuralNetwork extends Predictor {

    private final Matrix theta1;
    private final Matrix theta2;
    private final int numberOfNeuronsInTheOutputLayer;

    public NeuralNetwork(NNWeights weights) {
        this.theta1 = weights.getTheta1();
        this.theta2 = weights.getTheta2();
        this.numberOfNeuronsInTheOutputLayer = theta2.getNumberOfRows();
    }

    public static NeuralNetwork createNeuralNetwork(Matrix trainData, Matrix trainOutput, int hiddenLayerSize, double alpha, int numberOfIterations, double lambda) {

        HashSet<Double> classes = new HashSet<>();
        for (int i = 0; i < trainOutput.getNumberOfRows(); i++) classes.add(trainOutput.get(i, 0));
        int outputClassNumber = classes.size();
        Matrix theta1 = randInitTheta(hiddenLayerSize, trainData.getNumberOfColumns()+1, 123456l);
        Matrix theta2 = randInitTheta(outputClassNumber, hiddenLayerSize+1, 123456l);

        try {
            theta1 = Matrix.readFromFileDisk("test/resources/GradientDescentTestData/neuralNetwork/feedForwardTest/theta1_initial_test2.txt", " ");
            theta2 = Matrix.readFromFileDisk("test/resources/GradientDescentTestData/neuralNetwork/feedForwardTest/theta2_initial_test2.txt", " ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        NNWeights weights = new NNWeights(theta1, theta2);

        GenericGradientDescent<NeuralNetworkCostCalculator, NNWeights> algorithm = new GenericGradientDescent<>(alpha, trainData, trainOutput, weights);
        final NNWeights finalWeights = algorithm.performAlgorithm(new NeuralNetworkCostCalculator(outputClassNumber), numberOfIterations, lambda);

        return new NeuralNetwork(finalWeights);
    }

    public Matrix hypothesis(Matrix trainData) {

        Matrix h1 = trainData.introduceColumnOfOnesAtHead().multiply(theta1.transpose()).sigmoid();
        Matrix h0 = h1.introduceColumnOfOnesAtHead().multiply(theta2.transpose()).sigmoid();

        //System.out.println("\nh0 size:\nColumns:\t" + h0.getNumberOfColumns() + "\nRows:\t" + h0.getNumberOfRows());
        return h0;
    }

    public static double computeCost(Matrix trainData, Matrix expandedOut, NNWeights weights, double lambda) {

        NeuralNetwork nn = new NeuralNetwork(weights);
        Matrix hypothesis = nn.hypothesis(trainData);

        double regularisationFactor = NeuralNetwork.regularisationFactor(weights, lambda, trainData.getNumberOfRows());

        return LogisticRegressionCostCalculator.computeLogLoss(hypothesis, expandedOut, regularisationFactor);
    }

    public static double regularisationFactor(NNWeights weights, double lambda, double dataSetSize) {

        double pow = regularisationOn(weights.getTheta1()) + regularisationOn(weights.getTheta2());

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

    public int[] predictABounce(Matrix matrix) {

        if(this.numberOfNeuronsInTheOutputLayer <= 2) {
            throw new IllegalArgumentException("Binary classification not supported yet!");
        }else {
            Matrix result = hypothesis(matrix);
            int numberOfRows = result.getNumberOfRows();
            int numberOfColumns = result.getNumberOfColumns();
            int[] predictions = new int[numberOfRows];
            for (int i = 0; i < numberOfRows; i++) {
                double maxScore = -1;
                int maxScoreIndex = -1;
                for (int j = 0; j < numberOfColumns; j++) {
                    double value = result.get(i, j);
                    if(value > maxScore) {
                        maxScore = value;
                        maxScoreIndex = j + 1;
                    }
                }
                predictions[i] = maxScoreIndex;
            }
            return predictions;
        }
    }
}
