package neuralNetwork;

import linearAlgebra.Matrix;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dario on 07/04/15.
 */
public class BackPropagationTest {

    private static Matrix output;
    private static Matrix trainData;
    private static Matrix theta1;
    private static Matrix theta2;
    private static Matrix numericGradientForLambdaEqual0;
    private static Matrix numericGradientForLambdaEqual3;

    @BeforeClass
    public static void loadData() throws IOException {
        String basePath = "test/resources/GradientDescentTestData/neuralNetwork/backpropagationTest/";

        trainData = Matrix.readFromFileDisk(basePath + "test2_trainData.txt", " ");
        output = Matrix.readFromFileDisk(basePath + "test2_outData.txt", " ");

        theta1 = Matrix.readFromFileDisk(basePath + "test2_theta1.txt", " ");
        theta2 = Matrix.readFromFileDisk(basePath + "test2_theta2.txt", " ");

        numericGradientForLambdaEqual0 = Matrix.readFromFileDisk(basePath + "numgrad_lambda0.txt", " ");
        numericGradientForLambdaEqual3 = Matrix.readFromFileDisk(basePath + "numgrad_lambda3.txt", " ");
    }

    @Test
    public void testBackPropagation() {

        Matrix expandOutput = NeuralNetwork.expandOutput(output, 3);
        NNWeights weights = new NNWeights(theta1, theta2);
        double lambda = 0.0;
        checkThatBackPropagationProduceWhatExpected(expandOutput, weights, lambda, numericGradientForLambdaEqual0);

        lambda = 3.0;
        checkThatBackPropagationProduceWhatExpected(expandOutput, weights, lambda, numericGradientForLambdaEqual3);
    }

    private static void checkThatBackPropagationProduceWhatExpected(Matrix expandOutput, NNWeights initialWeights, double lambda, Matrix numericGradient) {

        System.out.println("Check weights produced with lambda = " + lambda);
        NNWeights nnWeights = BackPropagation.performAlgorithm(initialWeights, trainData, expandOutput, lambda);

        Matrix unrollParameters = unrollParameters(nnWeights.getTheta1(), nnWeights.getTheta2());

        Assert.assertTrue("Checking that the unrolled parameters and the validation parameters have the same number of rows",
                unrollParameters.getNumberOfRows() == numericGradient.getNumberOfRows());

        Assert.assertTrue("Checking that the unrolled parameters and the validation parameters have the same number of columns",
                unrollParameters.getNumberOfColumns() == numericGradient.getNumberOfColumns());

        final double error = 1e-9;

        for (int i = 0; i < numericGradient.getNumberOfRows(); i++) {
            for (int j = 0; j < numericGradient.getNumberOfColumns(); j++) {
                double expected = numericGradient.get(i, j);
                double actual = unrollParameters.get(i, j);
                Assert.assertEquals("The Back propagation algorithm has produced not expected result! for row = " + i ,
                        actual,
                        expected,
                        error);
            }
        }
    }

    private static Matrix unrollParameters(Matrix theta1, Matrix theta2) {
        ArrayList<Double> rawData = new ArrayList<>();

        unrollDataIntoAnArrayList(theta1, rawData);
        unrollDataIntoAnArrayList(theta2, rawData);

        double[][] data = new double[rawData.size()][1];

        for (int i = 0; i < rawData.size(); i++) {
            data[i] = new double[1];
            data[i][0] = rawData.get(i);
        }

        return new Matrix(data);
    }

    private static void unrollDataIntoAnArrayList(Matrix theta1, ArrayList<Double> rawData) {
        for (int i = 0; i < theta1.getNumberOfColumns(); i++) {
            for (int j = 0; j < theta1.getNumberOfRows(); j++) {
                rawData.add(theta1.get(j, i));
            }
        }
    }

    @Test
    public void introduceRowOfOnesAtHeadTest() {

        double[][] a = new double[][]{{2,3,4,5},{6,7,-1,9},{10,11,12,13}};

        Matrix matrix = new Matrix(a);
        System.out.println(matrix);
        Matrix matrix2 = matrix.introduceRowOfOnesAtHead();
        System.out.println("\n\nAfter modification:\n" + matrix2);

        Assert.assertTrue(matrix.getNumberOfColumns() == matrix2.getNumberOfColumns());
        Assert.assertTrue("The new matrix do not have an extra row!", matrix.getNumberOfRows()+1 == matrix2.getNumberOfRows());

        for (int i = 0; i < matrix2.getNumberOfColumns(); i++) {
            Assert.assertTrue(matrix2.get(0,i) == 1.0);
        }

        for (int i = 0; i < matrix.getNumberOfRows(); i++) {
            for (int j = 0; j < matrix.getNumberOfColumns(); j++) {
                Assert.assertTrue("The method has changed the state of the matrix! This can't happen!",
                        matrix.get(i, j) == matrix2.get(i+1, j)
                );
            }
        }
    }
}
