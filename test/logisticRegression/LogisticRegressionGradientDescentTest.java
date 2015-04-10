package logisticRegression;

import linearAlgebra.Matrix;
import linearAlgebra.MyConfusionMatrix;
import optimisationAlgorithms.Cost;
import optimisationAlgorithms.Weights;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dario on 23/01/15.
 */
public class LogisticRegressionGradientDescentTest {


    @Test
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

        LogisticRegression logisticRegression = LogisticRegression.createLogisticRegression(trainData, trainOutput, alpha, numberOfIterations, lambda);
        final boolean[] booleans = logisticRegression.predictABounce(testData);

        final MyConfusionMatrix confusionMatrix = createConfusionMatrix(booleans, test.getOutput());

        final double avgRecall = confusionMatrix.getAvgRecall();

        System.out.println(confusionMatrix);
        Assert.assertTrue("Asserting that the average recall is greater than 65%, it is instead " + avgRecall, avgRecall > 0.64);

    }


    private static MyConfusionMatrix createConfusionMatrix(boolean[] booleans, Matrix y) {

        ArrayList<Double> outputClassList = new ArrayList<>();
        outputClassList.add(0.0);
        outputClassList.add(1.0);

        MyConfusionMatrix cm = new MyConfusionMatrix(outputClassList);

        final int numberOfRows = y.getNumberOfRows();
        for (int i = 0; i < numberOfRows; i++) {
            final double rawActual = y.get(i, 0);
            final boolean prediction = booleans[i];
            double rawPrediction = 0.0;
            if (prediction) {
                rawPrediction = 1.0;
            }

            cm.addElement(rawPrediction, rawActual);
        }

        return cm;
    }


    @Test
    public void comprehensiveTest() throws IOException {


        String basePath = "test/resources/GradientDescentTestData/comprehensiveTest/";

        Matrix x = Matrix.readFromFileDisk(basePath + "x.txt", " ");
        Matrix y = Matrix.readFromFileDisk(basePath + "y.txt", " ");

        final double alpha = 0.01;
        final int numberOfIterations = 10_000;
        final double lambda = 0.1;

        LogisticRegression logisticRegression = LogisticRegression.createLogisticRegression(x, y, alpha, numberOfIterations, lambda);
        final boolean[] booleans = logisticRegression.predictABounce(x);

        final double naiveAccuracy = calculateNaiveAccuracy(booleans, y);
        System.out.println("Accuracy is:" + naiveAccuracy);

        Assert.assertTrue("Asserting that the accuracy is above 82.5%, it is actually " + naiveAccuracy, naiveAccuracy >= 0.825);

    }

    private static double calculateNaiveAccuracy(boolean[] booleans, Matrix y) {

        final int numberOfRows = y.getNumberOfRows();
        int accumulator = 0;
        for (int i = 0; i < numberOfRows; i++) {
            final double rawPrediction = y.get(i, 0);
            final boolean prediction = (rawPrediction == 1);
            if (prediction == booleans[i]) {
                accumulator++;
            }
        }

        return ((1.0*accumulator) / (1.0*numberOfRows));
    }

    @Test
    public void test1() throws IOException {

        String basePath = "test/resources/GradientDescentTestData/test1/";
        Matrix x = Matrix.readFromFileDisk(basePath + "x.txt", " ");
        Matrix y = Matrix.readFromFileDisk(basePath + "y.txt", " ");

        for (int i = 1; i <= 6; i++) {
            String currentPath = basePath + i + "/";
            readRestOfTheDataAndCompare(currentPath, x, y);
        }

    }

    @Test
    public void test2() throws IOException {

        String basePath = "test/resources/GradientDescentTestData/test2/";
        Matrix x = Matrix.readFromFileDisk(basePath + "x.txt", " ");
        Matrix y = Matrix.readFromFileDisk(basePath + "y.txt", " ");

        for (int i = 1; i <= 7; i++) {
            String currentPath = basePath + i + "/";
            readRestOfTheDataAndCompare(currentPath, x, y);
        }

    }

    private void readRestOfTheDataAndCompare(String basePath, Matrix x, Matrix y) throws IOException {

        Matrix j = Matrix.readFromFileDisk(basePath + "j.txt", " ");
        double cost = j.get(0, 0);
        Matrix grad = Matrix.readFromFileDisk(basePath + "grad.txt", " ");
        Matrix lambdaMatrix = Matrix.readFromFileDisk(basePath + "lambda.txt", " ");
        double lambda = lambdaMatrix.get(0, 0);
        Matrix theta = Matrix.readFromFileDisk(basePath + "theta.txt", " ");

        System.out.println("Location:\n" + basePath);
        System.out.println("cost: " + cost);
        System.out.println("lambda: " + lambda);

        compare(theta, x, y, lambda, grad, cost);
    }


    private static boolean compare(Matrix theta, Matrix x, Matrix y, double lambda, Matrix gradient, double cost) {

        LogisticRegressionCostCalculator logisticRegressionCostCalculator = new LogisticRegressionCostCalculator();
        Cost logisticRegressionCost = logisticRegressionCostCalculator.calculate(new LogisticRegressionWeights(theta), x, y, lambda);

        LogisticRegressionWeights weights = (LogisticRegressionWeights) logisticRegressionCost.getWeights();

        boolean equals1 = weights.getThetas().equals(gradient);
        boolean equals2 = logisticRegressionCost.getCost() == cost;

        return (equals1 && equals2);
    }
}
