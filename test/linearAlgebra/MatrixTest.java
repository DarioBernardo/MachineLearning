package linearAlgebra;

import org.jblas.DoubleMatrix;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dario on 23/01/15.
 */
public class MatrixTest {

    @Test
    public void testMatrixMultiplication() {

        double[][] m = new double[5][];
        m[0] = new double[] {17, 24, 1, 8, 15};
        m[1] = new double[] {23, 5, 7, 14, 16};
        m[2] = new double[] {4 ,6, 13, 20, 22};
        m[3] = new double[] {10, 12, 19, 21, 3};
        m[4] = new double[] {11, 18, 25, 2, 9};
        DoubleMatrix doubleMatrix = new DoubleMatrix(m);
        Matrix matrix1 = new Matrix(doubleMatrix);


        double[][] m2 = new double[5][];
        m2[0] = new double[] {14,3,25,7,32};
        m2[1] = new double[] {2,32,12,18,42};
        m2[2] = new double[] {1,3,9,4,23};
        m2[3] = new double[] {28,7,2,31,20};
        m2[4] = new double[] {9,2,42,5,18};

        Matrix matrix2 = new Matrix(new DoubleMatrix(m2));


        double[][] m3 = new double[5][];
        m3[0] = new double[] {238,72,25,56,480};
        m3[1] = new double[] {46 ,160, 84,252,672};
        m3[2] = new double[] {4,18,117,80,506};
        m3[3] = new double[] {280,84,38,651,60};
        m3[4] = new double[] {99,36,1050,10,162};

        Matrix validationMatrix = new Matrix(new DoubleMatrix(m3));

        Matrix multiplication = matrix1.multiplyElementWise(matrix2);

        Assert.assertTrue(validationMatrix.equals(multiplication));



        double[][] m4 = new double[5][];
        m4[0] = new double[] {646,908,1368,878,2005};
        m4[1] = new double[] {875 ,380, 1398,793,1675};
        m4[2] = new double[] {839,427,1253,918,1475};
        m4[3] = new double[] {798,624,733,1028,1735};
        m4[4] = new double[] {352,716,1098,608,1885};

        validationMatrix = new Matrix(new DoubleMatrix(m4));


        multiplication = matrix1.multiply(matrix2);
        Assert.assertTrue(validationMatrix.equals(multiplication));
    }

    @Test
    public void testMatrixOperations() {


        double[][] m = new double[5][];
        m[0] = new double[] {17, 24, 1, 8, 15};
        m[1] = new double[] {23, 5, 7, 14, 16};
        m[2] = new double[] {4 ,6, 13, 20, 22};
        m[3] = new double[] {10, 12, 19, 21, 3};
        m[4] = new double[] {11, 18, 25, 2, 9};

        DoubleMatrix doubleMatrix = new DoubleMatrix(m);
        Matrix matrix = new Matrix(doubleMatrix);

        double sum = matrix.sum();

        Assert.assertEquals(325.0, sum, 0.0);

        Matrix sigmoid = matrix.sigmoid();
        System.out.println(sigmoid);
    }

    @Test
    public void testMatrixSubSetOp() {

        double[][] m = new double[5][];
        m[0] = new double[] {17, 24, 1, 8, 15};
        m[1] = new double[] {23, 5, 7, 14, 16};
        m[2] = new double[] {4 ,6, 13, 20, 22};
        m[3] = new double[] {10, 12, 19, 21, 3};
        m[4] = new double[] {11, 18, 25, 2, 9};

        DoubleMatrix doubleMatrix = new DoubleMatrix(m);
        Matrix matrix = new Matrix(doubleMatrix);


        double[][] m2 = new double[3][];
        m2[0] = new double[] {5, 7, 14};
        m2[1] = new double[] {6, 13, 20};
        m2[2] = new double[] {12, 19, 21};

        Matrix validationMatrix = new Matrix(new DoubleMatrix(m2));

        Matrix subSet = matrix.subSet(1, 3, 1, 3);

        Assert.assertTrue(validationMatrix.equals(subSet));



        double[][] m3 = new double[3][];
        m3[0] = new double[] {7, 14, 16};
        m3[1] = new double[] {13, 20, 22};
        m3[2] = new double[] {19, 21, 3};
        validationMatrix = new Matrix(new DoubleMatrix(m3));

        subSet = matrix.subSet(1, 3, 2, 4);

        Assert.assertTrue(validationMatrix.equals(subSet));


        double[][] m4 = new double[4][];
        m4[0] = new double[] {23, 5, 7, 14, 16};
        m4[1] = new double[] {4 ,6, 13, 20, 22};
        m4[2] = new double[] {10, 12, 19, 21, 3};
        m4[3] = new double[] {11, 18, 25, 2, 9};
        validationMatrix = new Matrix(new DoubleMatrix(m4));

        subSet = matrix.removeFirstRow();
        Assert.assertTrue(validationMatrix.equals(subSet));

    }
}

