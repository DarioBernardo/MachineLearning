package logisticRegression;

import linearAlgebra.Matrix;
import optimisationAlgorithms.Cost;
import optimisationAlgorithms.CostCalculator;
import optimisationAlgorithms.Weights;

/**
 * Created by dario on 08/01/15.
 */
public class LogisticRegressionCostCalculator implements CostCalculator<LogisticRegressionWeights>{


    public Cost calculate(LogisticRegressionWeights weights, Matrix x, Matrix y, double lambda) {

        Matrix theta = weights.getThetas();
        double m = x.rowCount();
        double regularisationFactor = LogisticRegression.regularisationFactor(theta, lambda, m);

        LogisticRegression logisticRegression = new LogisticRegression(new LogisticRegressionWeights(theta));
        Matrix h0 = logisticRegression.hypothesis(x);
        double costJ = computeLogLoss(h0, y, regularisationFactor);

        Matrix thetaCopy = theta.copy();
        thetaCopy.set(0, 0, 0);
        Matrix regularisationFactorForGradient = thetaCopy.multiply(lambda / m);
        Matrix gradient = h0.subtract(y).transpose().multiply((x)).transpose().multiply(1 / m).add(regularisationFactorForGradient);

        return new Cost(costJ, new LogisticRegressionWeights(gradient));
    }

    public static double computeLogLoss(Matrix h0, Matrix y, double regularisationFactor) {
        double m = h0.rowCount();
        Matrix lh0 = y.negate().multiplyElementWise(h0.log());
        Matrix m1lh0 = y.oneMinusThis().multiplyElementWise(h0.oneMinusThis().log());
        return (1/m) * lh0.subtract(m1lh0).sum() + regularisationFactor;
    }
}
