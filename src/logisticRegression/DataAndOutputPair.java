package logisticRegression;

import linearAlgebra.Matrix;

/**
 * Created by dario on 23/01/15.
 */
public class DataAndOutputPair {


    private final Matrix data;
    private final Matrix output;

    public DataAndOutputPair(Matrix data, Matrix output) {
        this.data = data;
        this.output = output;
    }

    public Matrix getData() {
        return data;
    }

    public Matrix getOutput() {
        return output;
    }
}
