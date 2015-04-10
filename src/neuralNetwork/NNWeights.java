package neuralNetwork;

import linearAlgebra.Matrix;
import optimisationAlgorithms.Weights;

/**
 * Created by dario on 07/04/15.
 */
public class NNWeights implements Weights<NNWeights> {

    private Matrix theta1;
    private Matrix theta2;

    public NNWeights(Matrix theta1, Matrix theta2) {
        this.theta1 = theta1;
        this.theta2 = theta2;
    }

    public Matrix getTheta1() {
        return theta1;
    }

    public Matrix getTheta2() {
        return theta2;
    }

    @Override
    public void updateWeights(NNWeights gradient, double alpha) {
        theta1 = theta1.subtract((gradient.getTheta1().transpose().multiply(alpha)));
        theta2 = theta2.subtract((gradient.getTheta2().transpose().multiply(alpha)));
    }
}
