package linearAlgebra;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dario on 23/01/15.
 */
public class MyConfusionMatrix {

    private final int[][] matrix;
    private final List<Double> classList;
    private int unFired;

    public MyConfusionMatrix(List<Double> encodedOutClass) {
        this.classList = encodedOutClass;
        this.matrix = new int[classList.size()][classList.size()];
        this.unFired = 0;
    }

    public synchronized void addElement(Double predicted, Double actual) {
        int index = classList.indexOf(actual);
        if (index == -1) {
            throw new IllegalArgumentException("Cannot find 'actual' value in confusion matrix: " + print(actual) + ", allowed encoded values are: " + print(classList));
        }
        if (predicted.equals(actual)) {
            matrix[index][index]++;
        } else {
            int predictedIndex = classList.indexOf(predicted);
            if (predictedIndex == -1) {
                throw new IllegalArgumentException("Cannot find 'predicated' value in confusion matrix: " + print(predicted) + ", allowed encoded values are: " + print(classList));
            }

            matrix[index][predictedIndex]++;
        }
    }

    private static String print(List<Double> classList) {
        StringBuffer stringBuffer = new StringBuffer();
        classList.forEach(each -> stringBuffer.append(print(each)).append(", "));
        return stringBuffer.toString();
    }

    private static String print(Double actual) {
        return new BigDecimal(actual).toPlainString();
    }

    protected int getElement(Double predicted, Double actual) {
        int actualIndex = classList.indexOf(actual);
        int predictedIndex = classList.indexOf(predicted);
        return matrix[actualIndex][predictedIndex];
    }

    public double[] getRecall() {
        double[] recall = new double[classList.size()];

        for (int i = 0; i < classList.size(); i++) {
            double tot = 0.0;
            for (int j = 0; j < classList.size(); j++) {
                tot += matrix[i][j];
            }
            recall[i] = (matrix[i][i] / tot);
        }

        return recall;
    }

    public double getAvgRecall() {

        double[] recall = getRecall();

        double result = 0.0;
        for (double r : recall) {
            result += r;
        }

        return result / recall.length;
    }


    public synchronized void inreaseUnfired() {
        this.unFired++;
    }

    /**
     * This method return the number of times the similarity were used to classify
     */
    public int getNumberOfMisclassifications() {
        return this.unFired;
    }

    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();
        buf.append("\n\t\t\t\tPredicted Class");
        buf.append("\n\t\t\t");

        for (int i = 0; i < classList.size(); ++i) {
            buf.append(classList.get(i) + "\t\t");
        }
        buf.append("(TOT)\t(RECALL)\n");

        double[] sum = new double[classList.size()];

        int totActual = 0;
        int ins = classList.size() / 2;
        double totRecall = 0.0;
        double totPrecision = 0.0;
        double numAccuracy = 0.0;
        double denumAccuracy = 0.0;

        for (int i = 0; i < classList.size(); i++) {
            if (i == ins) {
                buf.append("\nActual\t");
            } else {
                buf.append("\n\t");
            }

            buf.append(classList.get(i) + "\t\t");

            double tot = 0.0;
            for (int j = 0; j < classList.size(); j++) {
                buf.append(matrix[i][j] + "\t\t");
                tot += matrix[i][j];
                sum[j] += matrix[i][j];

                denumAccuracy += matrix[i][j];
                if (i == j) {
                    numAccuracy += matrix[i][j];
                }
            }
            double recall = (matrix[i][i] / tot);
            totRecall += recall;
            buf.append(tot + "\t" + recall);

            totActual += tot;
        }

        buf.append("\n\n\t\t\t");
        for (int j = 0; j < classList.size(); j++) {
            buf.append(sum[j] + "\t\t");
        }

        buf.append("\n(PRECISION)\t\t\t");
        for (int j = 0; j < classList.size(); j++) {
            double jTHprecision = (matrix[j][j] / sum[j]);
            totPrecision += jTHprecision;
            buf.append(jTHprecision + "\t\t");
        }

        buf.append("\n\n(AVG REC)\t\t\t" + (totRecall / classList.size()));
        buf.append("\n(AVG PREC)\t\t\t" + (totPrecision / classList.size()));
        buf.append("\n(ACCURACY)\t\t\t" + (numAccuracy / denumAccuracy));
        buf.append("\n(UNFIRED )\t\t\t").append(this.unFired).append(" out of ").append(totActual);

        return buf.toString();
    }
}
