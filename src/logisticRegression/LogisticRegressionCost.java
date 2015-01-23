package logisticRegression;

import linearAlgebra.Matrix;

/**
 * Created by dario on 08/01/15.
 */
public class LogisticRegressionCost {

    private final Matrix gradient;
    private final double cost;


    public LogisticRegressionCost(Matrix gradient, double cost) {
        this.gradient = gradient;
        this.cost = cost;
    }

    public Matrix getGradient() {
        return gradient;
    }

    public double getCost() {
        return cost;
    }

    public static LogisticRegressionCost create(Matrix theta, Matrix x, Matrix y, double lambda) {

        double m = x.rowCount();
        double regularisationFactor = LogisticRegression.regularisationFactor(theta, lambda, m);

        Matrix h0 = LogisticRegression.hypothesis(x, theta);
        Matrix lh0 = y.negate().multiplyElementWise(h0.log());
        Matrix m1lh0 = y.oneMinusThis().multiplyElementWise(h0.oneMinusThis().log());
        double costJ = (1/m) * lh0.subtract(m1lh0).sum() + regularisationFactor;

        Matrix thetaCopy = theta.copy();
        thetaCopy.set(0, 0, 0);
        Matrix regularisationFactorForGradient = thetaCopy.multiply(lambda / m);
        Matrix gradient = h0.subtract(y).transpose().multiply((x)).transpose().multiply(1 / m).add(regularisationFactorForGradient);

        return new LogisticRegressionCost(gradient, costJ);
    }
}
