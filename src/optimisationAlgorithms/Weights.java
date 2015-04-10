package optimisationAlgorithms;

/**
 * Created by dario on 09/04/15.
 */
public interface Weights<T extends Weights> {

    void updateWeights(T gradient, double alpha);
}
