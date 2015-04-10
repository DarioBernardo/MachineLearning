package logisticRegression;

import linearAlgebra.Matrix;
import optimisationAlgorithms.Weights;

/**
 * Created by dario on 09/04/15.
 */
public class LogisticRegressionWeights implements Weights<LogisticRegressionWeights> {

    Matrix thetas;

    public LogisticRegressionWeights(Matrix thetas) {
        this.thetas = thetas;
    }

    public Matrix getThetas() {
        return thetas;
    }

    @Override
    public void updateWeights(LogisticRegressionWeights gradient, double alpha) {

        thetas = thetas.subtract((gradient.getThetas().transpose().multiply(alpha)));
    }
}
