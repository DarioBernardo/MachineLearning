package linearAlgebra;

import logisticRegression.DataAndOutputPair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by dario on 23/01/15.
 */
public class Matrix {

    private final DoubleMatrix doubleMatrix;

    public Matrix(DoubleMatrix doubleMatrix) {
        this.doubleMatrix = doubleMatrix;
    }

    public Matrix(double[][] doubleMatrix) {
        this.doubleMatrix = new DoubleMatrix(doubleMatrix);
    }

    public static Matrix create(List<double[]> doubles) {
        double[][] rawMatrix = new double[doubles.size()][];
        int i = 0;
        for (double[] aDoubleArray : doubles) {
            rawMatrix[i] = aDoubleArray;
            i++;
        }
        DoubleMatrix m = new DoubleMatrix(rawMatrix);
        return new Matrix(m);
    }

    public static Matrix readFromFileDisk(String path, String delimType) throws IOException {

        final ArrayList<double[]> list = new ArrayList<>();
        Stream<String> lines = Files.lines(new File(path).toPath());
        lines.forEach(line -> {
            if (!line.isEmpty() && line.charAt(0) != '#') {
                String[] splitLine = line.trim().split(delimType);
                double[] numbers = new double[splitLine.length];
                for (int i = 0; i < splitLine.length; i++) {
                    numbers[i] = Double.parseDouble(splitLine[i].trim());
                }
                list.add(numbers);
            }
        });

        return Matrix.create(list);
    }

    public static DataAndOutputPair readFromCSV(String path, char delimType) throws IOException {

        //Create the CSVFormat object
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(delimType);

        //initialize the CSVParser object
        CSVParser parser = new CSVParser(new FileReader(path), format);

        ArrayList<double[]> data = new ArrayList<>();
        ArrayList<double[]> output = new ArrayList<>();
        Iterator<CSVRecord> iterator = parser.iterator();
        while (iterator.hasNext()) {
            CSVRecord record = iterator.next();
            final int size = record.size();
            double[] row = new double[size-1];
            double[] out = new double[1];

            //Read the data
            for (int i = 0; i < size-1; i++) {
                String colValue = record.get(i);
                row[i] = Double.parseDouble(colValue);
            }

            //Read the output
            String colValue = record.get(size-1);
            out[0] = Double.parseDouble(colValue);

            output.add(out);
            data.add(row);
        }

        parser.close();

        return new DataAndOutputPair(Matrix.create(data), Matrix.create(output));
    }

    public Matrix multiplyElementWise(Matrix columnY) {
        return new Matrix(doubleMatrix.mul(columnY.doubleMatrix));
    }
    public Matrix multiply(Matrix columnY) {
        return new Matrix(doubleMatrix.mmul(columnY.doubleMatrix));
    }


    public Matrix multiply(double scalar) {
        return new Matrix(this.doubleMatrix.mul(scalar));
    }

    public Matrix squared() {
        return new Matrix(MatrixFunctions.pow(doubleMatrix, 2));
    }

    public Matrix transpose() {
        return new Matrix(doubleMatrix.transpose());
    }

    public double sum() {
        return doubleMatrix.sum();
    }

    public Matrix add(Matrix matrix) {
        return new Matrix(this.doubleMatrix.add(matrix.doubleMatrix));
    }

    public Matrix subtractEachElementWith(double c) {
        return new Matrix(this.doubleMatrix.sub(c));
    }

    public Matrix subtract(Matrix c) {

        return new Matrix(this.doubleMatrix.sub(c.doubleMatrix));
    }

    public Matrix oneMinusThis() {
        DoubleMatrix ones = DoubleMatrix.ones(doubleMatrix.rows, doubleMatrix.columns);
        return new Matrix(ones.sub(doubleMatrix));
    }


    public double rowCount() {
        return doubleMatrix.rows;
    }

    public Matrix removeFirstRow() {
        return subSet(1, doubleMatrix.getRows()-1, 0, doubleMatrix.getColumns()-1);
    }

    public Matrix subSet(int rowStart, int rowEnd, int columnStart, int columnEnd) {
        double[] rows = new double[doubleMatrix.getRows()];
        for (int i = rowStart; i <= rowEnd; i++) {
            rows[i] = 1;
        }

        double[] cols = new double[doubleMatrix.getColumns()];
        for (int i = columnStart; i <= columnEnd; i++) {
            cols[i] = 1;
        }

        return new Matrix(doubleMatrix.get(new DoubleMatrix(rows), new DoubleMatrix(cols)));
    }

    public Matrix sigmoid() {
        DoubleMatrix ones = DoubleMatrix.ones(doubleMatrix.rows, doubleMatrix.columns);
        return new Matrix(ones.div(ones.add(MatrixFunctions.exp(doubleMatrix.neg()))));
    }

    public Matrix log() {
        return new Matrix(MatrixFunctions.log(doubleMatrix));
    }

    public Matrix negate() {
        return new Matrix(this.doubleMatrix.neg());
    }

    public double get(int row, int column) {
        return this.doubleMatrix.get(row, column);
    }

    public void set(int row, int col, int value) {
        this.doubleMatrix.put(row, col, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix)) return false;

        Matrix matrix = (Matrix) o;

        if (!doubleMatrix.equals(matrix.doubleMatrix)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return doubleMatrix.hashCode();
    }

    public Matrix copy() {
        return new Matrix(new DoubleMatrix(doubleMatrix.toArray2()));
    }

    @Override
    public String toString() {

        StringBuilder b = new StringBuilder();
        b.append("Matrix:\n");

        for (int j = 0; j < doubleMatrix.getRows(); j++) {
            for (int i = 0; i < doubleMatrix.getColumns(); i++) {
                double value = doubleMatrix.get(j, i);
                b.append(value);
                if(i < doubleMatrix.getColumns()-1) {
                    b.append(",\t");
                }
            }
            b.append("\n");
        }

        return b.toString();
    }

    public int getNumberOfRows() {
        return this.doubleMatrix.getRows();
    }


    public int getNumberOfColumns() {
        return this.doubleMatrix.getColumns();
    }

    public void scale() {

        final DoubleMatrix maxs = this.doubleMatrix.columnMaxs();
        final DoubleMatrix mins = this.doubleMatrix.columnMins();
        final DoubleMatrix columnMeans = this.doubleMatrix.columnMeans();

        final int rows = this.doubleMatrix.rows;
        final int columns = this.doubleMatrix.columns;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double value = this.doubleMatrix.get(i, j);
                double scaledValue = (value - columnMeans.get(j)) / (maxs.get(j)-mins.get(j));
                this.doubleMatrix.put(i, j, scaledValue);
            }
        }

    }

    public Matrix introduceColumnOfOnesAtHead() {
        int numberOfRows = this.getNumberOfRows();
        int numberOfColumns = this.getNumberOfColumns();
        DoubleMatrix result = new DoubleMatrix(numberOfRows, numberOfColumns +1);

        for (int i = 0; i < numberOfRows; i++) {
            result.put(i, 0, 1.0);
            for (int j = 0; j < numberOfColumns; j++) {
                result.put(i, j+1, doubleMatrix.get(i, j));
            }
        }
        return new Matrix(result);
    }
}

