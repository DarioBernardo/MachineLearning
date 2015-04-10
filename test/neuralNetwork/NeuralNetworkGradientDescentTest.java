package neuralNetwork;

import linearAlgebra.Matrix;
import linearAlgebra.MyConfusionMatrix;
import logisticRegression.DataAndOutputPair;
import logisticRegression.LogisticRegressionGradientDescentTest;
import optimisationAlgorithms.Cost;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by dario on 10/04/15.
 */
public class NeuralNetworkGradientDescentTest {

    private static Matrix output;
    private static Matrix trainData;
    private static final String basePath = "test/resources/GradientDescentTestData/neuralNetwork/feedForwardTest/";

    @BeforeClass
    public static void loadData() throws IOException {
        DataAndOutputPair train = Matrix.readFromCSV(basePath + "numbersPicture.csv", '\t');

        output = train.getOutput();
        trainData = train.getData();
    }


    @Test
    @Ignore
    public void lendingClubTest() throws IOException {

        String basePath = "test/resources/GradientDescentTestData/LendingClub/";

        DataAndOutputPair train = Matrix.readFromCSV(basePath + "TRAIN_Encoded_Data.csv", '\t');
        DataAndOutputPair test = Matrix.readFromCSV(basePath + "TEST_Encoded_Data.csv", '\t');

        final Matrix trainData = train.getData();
        trainData.scale();
        final Matrix testData = test.getData();
        testData.scale();
        final Matrix trainOutput = train.getOutput();

        System.out.println("Number of columns: " + trainData.getNumberOfColumns());

        Assert.assertEquals(1, trainOutput.getNumberOfColumns());

        final double alpha = 0.01;
        final int numberOfIterations = 12_000;
        final double lambda = 0.05;
        final int hiddenLayerSize = 25;

        NeuralNetwork neuralNetwork = NeuralNetwork.createNeuralNetwork(trainData, trainOutput, hiddenLayerSize, alpha, numberOfIterations, lambda);
        final double avgRecall = calculateAvgRecall(testData, test.getOutput(), neuralNetwork);
        Assert.assertTrue("Asserting that the average recall is greater than 65%, it is instead " + avgRecall, avgRecall > 0.64);

    }

    private static double calculateAvgRecall(Matrix testData, Matrix testOutput, NeuralNetwork neuralNetwork) {
        final int[] predictions = neuralNetwork.predictABounce(testData);

        final MyConfusionMatrix confusionMatrix = createConfusionMatrix(predictions, testOutput);

        final double avgRecall = confusionMatrix.getAvgRecall();

        System.out.println(confusionMatrix);
        return avgRecall;
    }

    public static MyConfusionMatrix createConfusionMatrix(int[] predictions, Matrix y) {

        HashSet<Double> outputClassSet = new HashSet<>();
        for (int i = 0; i < y.getNumberOfRows(); i++) {
            outputClassSet.add(y.get(i, 0));
        }

        ArrayList<Double> encodedOutClass = new ArrayList<>(outputClassSet);
        Collections.sort(encodedOutClass);
        MyConfusionMatrix cm = new MyConfusionMatrix(encodedOutClass);

        final int numberOfRows = y.getNumberOfRows();
        for (int i = 0; i < numberOfRows; i++) {
            final double rawActual = y.get(i, 0);
            final double rawPrediction = predictions[i];

            cm.addElement(rawPrediction, rawActual);
        }

        return cm;
    }

    @Test
    @Ignore
    public void numberRecognitionTest() throws IOException {

        //This test is not broken

        final double alpha = 0.01;
        final int numberOfIterations = 500;
        final double lambda = 1;
        final int hiddenLayerSize = 25;

        NeuralNetwork neuralNetwork = NeuralNetwork.createNeuralNetwork(trainData, output, hiddenLayerSize, alpha, numberOfIterations, lambda);
        final double avgRecall = calculateAvgRecall(trainData, output, neuralNetwork);

        System.out.println("Average Recall: " + avgRecall);
    }

    @Test
    public void gradientDescentTest1() throws IOException {

        Matrix theta1 = Matrix.readFromFileDisk(basePath + "theta1_initial_test1.txt", " ");
        Matrix theta2 = Matrix.readFromFileDisk(basePath + "theta2_initial_test1.txt", " ");

        Matrix theta1Actual = Matrix.readFromFileDisk(basePath + "test2_theta1_actual.txt", " ");
        Matrix theta2Actual = Matrix.readFromFileDisk(basePath + "test2_theta2_actual.txt", " ");

        final double lambda = 0.5;

        int outClassesNumber = 10;
        NeuralNetworkCostCalculator neuralNetworkCostCalculator = new NeuralNetworkCostCalculator(outClassesNumber);
        Cost cost = neuralNetworkCostCalculator.calculate(new NNWeights(theta1, theta2), trainData, output, lambda);

        System.out.println("Cost: " + cost.getCost());
        Assert.assertEquals(6.8439, cost.getCost(), 0.00005);

        NNWeights weights = (NNWeights) cost.getWeights();
        Matrix finalTheta1 = weights.getTheta1();
        System.out.println(finalTheta1);
        Matrix finalTheta2 = weights.getTheta2();
        System.out.println(finalTheta2);

        System.out.println("Checking theta1:");
        BackPropagationTest.checkThatTwoMatricesAreTheSame(finalTheta1, theta1Actual);
        System.out.println("OK");
        System.out.println("Checking theta2:");
        BackPropagationTest.checkThatTwoMatricesAreTheSame(finalTheta2, theta2Actual);
        System.out.println("OK");
    }

    @Test
    public void gradientDescentTest2() throws IOException {

        Matrix theta1 = Matrix.readFromFileDisk(basePath + "theta1_initial_test2.txt", " ");
        Matrix theta2 = Matrix.readFromFileDisk(basePath + "theta2_initial_test2.txt", " ");

        final double lambda = 0.5;

        int outClassesNumber = 10;
        NeuralNetworkCostCalculator neuralNetworkCostCalculator = new NeuralNetworkCostCalculator(outClassesNumber);
        Cost cost = neuralNetworkCostCalculator.calculate(new NNWeights(theta1, theta2), trainData, output, lambda);

        System.out.println("Cost: " + cost.getCost());
        Assert.assertEquals(6.78906, cost.getCost(), 0.00005);
    }

        @Test
    public void predictionTest() throws IOException {

        Matrix theta1Final = Matrix.readFromFileDisk(basePath + "final_theta1.txt", " ");
        Matrix theta2Final = Matrix.readFromFileDisk(basePath + "final_theta2.txt", " ");

        NeuralNetwork nn = new NeuralNetwork(new NNWeights(theta1Final, theta2Final));

        final double avgRecall = calculateAvgRecall(trainData, output, nn);

        System.out.println("\n\nAverage Recall: " + avgRecall);

        Assert.assertEquals("The prediction phase do not achieve the correct accuracy ", 0.9346, avgRecall, 1e-10);
    }
}
