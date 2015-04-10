package optimisationAlgorithms;

import linearAlgebra.Matrix;

/**
 * Created by dario on 08/04/15.
 */
public interface CostCalculator<T extends Weights> {

    public Cost calculate(T theta, Matrix x, Matrix y, double lambda);
}
