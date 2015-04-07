package neuralNetwork;

import linearAlgebra.Matrix;
import logisticRegression.DataAndOutputPair;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by dario on 05/04/15.
 */
public class FeedForwardTest {

    private static Matrix output;
    private static Matrix trainData;
    private static Matrix theta1;
    private static Matrix theta2;

    @BeforeClass
    public static void loadData() throws IOException {
        String basePath = "test/resources/GradientDescentTestData/neuralNetwork/";
        DataAndOutputPair train = Matrix.readFromCSV(basePath + "numbersPicture.csv", '\t');

        output = train.getOutput();
        trainData = train.getData();

        theta1 = Matrix.readFromFileDisk(basePath + "theta1.txt", " ");
        theta2 = Matrix.readFromFileDisk(basePath + "theta2.txt", " ");
    }

    @Test
    public void computeCostTest() throws IOException {

        System.out.println("\nData size:\nColumns:\t" + trainData.getNumberOfColumns() + "\nRows:\t" + trainData.getNumberOfRows());
        System.out.println("\nTheta1 size:\nColumns:\t" + theta1.getNumberOfColumns() + "\nRows:\t" + theta1.getNumberOfRows());
        System.out.println("\nTheta2 size:\nColumns:\t" + theta2.getNumberOfColumns() + "\nRows:\t" + theta2.getNumberOfRows());

        double cost = NeuralNetwork.computeCost(trainData, output, theta1, theta2, 0.0);
        Assert.assertEquals(0.287629, cost, 0.0000009);
        System.out.println("\n\nCOST (Lambda = 0): " + cost);

        double lambda = 1.0;
        double regularisationFactor = NeuralNetwork.regularisationFactor(theta1, theta2, lambda, trainData.getNumberOfRows());
        Assert.assertEquals(0.096141, regularisationFactor, 0.0000009);

        cost = NeuralNetwork.computeCost(trainData, output, theta1, theta2, lambda);
        Assert.assertEquals(0.383770, cost, 0.0000009);
        System.out.println("\n\nCOST (Lambda = 1.0): " + cost);


        lambda = 0.5;
        regularisationFactor = NeuralNetwork.regularisationFactor(theta1, theta2, lambda, trainData.getNumberOfRows());
        Assert.assertEquals(0.048070, regularisationFactor, 0.0000009);

        cost = NeuralNetwork.computeCost(trainData, output, theta1, theta2, lambda);
        Assert.assertEquals(0.335700, cost, 0.0000009);
        System.out.println("\n\nCOST (Lambda = 0.5): " + cost);
    }

    @Test
    public void randInitThetaTest() {

        final double randomInitialValueRange = 0.12;

        Matrix matrix = NeuralNetwork.randInitTheta(10, 4, 123567l);
        for (int i = 0; i < matrix.getNumberOfRows(); i++) {
            for (int j = 0; j < matrix.getNumberOfColumns(); j++) {
                double v = matrix.get(i, j);
                Assert.assertTrue((v > -randomInitialValueRange && v < randomInitialValueRange));
            }
        }
    }
}
