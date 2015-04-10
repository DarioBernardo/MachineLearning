package neuralNetwork;

import linearAlgebra.Matrix;

import java.util.Arrays;

/**
 * Created by dario on 07/04/15.
 */
public class BackPropagation {


    public static NNWeights performAlgorithm(NNWeights currentWeights, Matrix trainingData, Matrix outputExpanded, double lambda) {

        Matrix theta1 = currentWeights.getTheta1();
        Matrix theta2 = currentWeights.getTheta2();

        Matrix accumulator1 = Matrix.createEmptyMatrix(theta1.getNumberOfRows(), theta1.getNumberOfColumns());
        Matrix accumulator2 = Matrix.createEmptyMatrix(theta2.getNumberOfRows(), theta2.getNumberOfColumns());

        int m = trainingData.getNumberOfRows();
        if(outputExpanded.getNumberOfRows() != m) {
            throw new IllegalArgumentException("The output matrix should have the same number of rows than the training data matrix");
        }

        for (int i = 0; i < m; i++) {
            Matrix a1 = trainingData.getRow(i).introduceColumnOfOnesAtHead().transpose();
            Matrix z2 = theta1.multiply(a1);
            Matrix a2 = z2.sigmoid().introduceRowOfOnesAtHead();

            Matrix a3 = theta2.multiply(a2).sigmoid();
            Matrix delta3 = a3.subtract(outputExpanded.getRow(i).transpose());
            Matrix delta2 = theta2.transpose().removeFirstRow().multiply(delta3).multiplyElementWise(z2.sigmoidGradient());

            accumulator1 = accumulator1.add(delta2.multiply(a1.transpose()));
            accumulator2 = accumulator2.add(delta3.multiply(a2.transpose()));
        }


        Matrix theta1Grad = accumulator1.divideElementsWise(m);
        Matrix theta2Grad = accumulator2.divideElementsWise(m);

        Matrix gradient1Regularization = theta1.removeFirstColumn().multiply(lambda / m);
        Matrix gradient2Regularization = theta2.removeFirstColumn().multiply(lambda / m);

        addRegularisationToTheta(theta1Grad, gradient1Regularization);
        addRegularisationToTheta(theta2Grad, gradient2Regularization);

        return new NNWeights(theta1Grad, theta2Grad);
    }

    private static void addRegularisationToTheta(Matrix theta1Grad, Matrix gradient1Regularization) {

        int numberOfRows = theta1Grad.getNumberOfRows();
        int numberOfColumns = theta1Grad.getNumberOfColumns();

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 1; j < numberOfColumns; j++) {
                double unregularizedValue = theta1Grad.get(i, j);
                double regularizationValue = gradient1Regularization.get(i, j - 1);
                theta1Grad.set(i, j, (unregularizedValue + regularizationValue));
            }
        }
    }

}
