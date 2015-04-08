package neuralNetwork;

import linearAlgebra.Matrix;

/**
 * Created by dario on 07/04/15.
 */
public class NNWeights {

    private final Matrix theta1;
    private final Matrix theta2;

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
}
