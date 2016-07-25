package vn.tribt.ai.ann.libs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Matrix implements MatrixMath {
    private double[][] matrix;
    private int rows;
    private int cols;

    public static final String MARK = "x";

    public Matrix() {
        // do nothing here
    }

    public Matrix(Matrix m, boolean keepData) {
        rows = m.getRows();
        cols = m.getCols();

        if (keepData) {
            matrix = m.getMatrix();
        } else {
            matrix = new double[rows][cols];
        }
    }

    public Matrix(int rows, int cols, boolean random) {
        matrix = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;

        if (random) {
            double min = -1.0;
            double max = 1.0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = Math.random() * (max - min) + min;
                }
            }
        }
    }

    public Matrix(double[][] matrix) {
        if (matrix == null) {
            System.err.println("ERROR: parameter matrix[][] is NULL");
        } else {
            this.matrix = matrix;
            rows = matrix.length;
            cols = matrix[0].length;
        }
    }

    public Matrix getColumn(int col) {
        double[][] data = new double[rows][1];
        for (int i = 0; i < rows; i++) {
            data[i][0] = matrix[i][col];
        }
        return new Matrix(data);
    }

    public Matrix getRow(int row) {
        double[][] data = new double[1][cols];
        for (int j = 0; j < cols; j++) {
            data[0][j] = matrix[row][j];
        }
        return new Matrix(data);
    }

    public Matrix removeColumn(int col) {
        if (col > cols - 1 || cols < 0) {
            System.err.println(
                    "ERROR: cannot remove this column, condition: 0 <= index <= "
                            + (cols - 1));
            return null;
        }

        double[][] temp = new double[rows][cols - 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                temp[i][j] = matrix[i][j];
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = col; j < cols - 1; j++) {
                temp[i][j] = matrix[i][j + 1];
            }
        }

        return new Matrix(temp);
    }

    public Matrix removeRow(int row) {
        if (row > rows - 1 || row < 0) {
            System.err.println(
                    "ERROR: cannot remove this row, condition: 0 <= index <= "
                            + (rows - 1));
            return null;
        }

        double[][] temp = new double[rows - 1][cols];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i][j];
            }
        }

        for (int i = row; i < rows - 1; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i + 1][j];
            }
        }

        return new Matrix(temp);
    }

    public Matrix addColumn(int col) {
        if (col > cols || cols < 0) {
            System.err.println(
                    "ERROR: cannot add a new column, condition: 0 <= index <= "
                            + cols);
            return null;
        }

        double[][] temp = new double[rows][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                temp[i][j] = matrix[i][j];
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = col; j < cols; j++) {
                temp[i][j + 1] = matrix[i][j];
            }
        }

        matrix = temp;
        cols = cols + 1;
        return this;
    }

    public Matrix addColumn(int col, double val) {
        double[] temp = new double[rows];
        for (int i = 0; i < rows; i++) {
            temp[i] = val;
        }
        return addColumn(col, temp);
    }

    public Matrix addColumn(int col, double[] val) {
        if (val.length != rows) {
            System.err.println(
                    "ERROR: cannot add a new column, the length of val[] must be equal "
                            + rows);
            return null;
        }

        if (col > cols || cols < 0) {
            System.err.println(
                    "ERROR: cannot add a new column, condition: 0 <= index <= "
                            + cols);
            return null;
        }

        double[][] temp = new double[rows][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                temp[i][j] = matrix[i][j];
            }
        }

        for (int i = 0; i < rows; i++) {
            temp[i][col] = val[i];
        }

        for (int i = 0; i < rows; i++) {
            for (int j = col; j < cols; j++) {
                temp[i][j + 1] = matrix[i][j];
            }
        }

        matrix = temp;
        cols = cols + 1;
        return this;
    }

    public Matrix addRow(int row) {
        if (row > rows || row < 0) {
            System.err.println(
                    "ERROR: cannot add a new row, condition: 0 <= index <= "
                            + rows);
            return null;
        }

        double[][] temp = new double[rows + 1][cols];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i][j];
            }
        }

        for (int i = row; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i + 1][j] = matrix[i][j];
            }
        }

        matrix = temp;
        rows = rows + 1;
        return this;
    }

    public Matrix addRow(int row, double val) {
        double[] data = new double[cols];
        for (int i = 0; i < cols; i++) {
            data[i] = val;
        }
        return addRow(row, data);
    }

    public Matrix addRow(int row, double[] val) {
        if (val.length != cols) {
            System.err.println(
                    "ERROR: cannot add a new row, the length of val[] must be equal "
                            + cols);
            return null;
        }

        if (row > rows || row < 0) {
            System.err.println(
                    "ERROR: cannot add a new row, condition: 0 <= index <= "
                            + rows);
            return null;
        }

        double[][] temp = new double[rows + 1][cols];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i][j];
            }
        }

        for (int i = 0; i < cols; i++) {
            temp[row][i] = val[i];
        }

        for (int i = row; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i + 1][j] = matrix[i][j];
            }
        }

        matrix = temp;
        rows = rows + 1;
        return this;
    }

    public Matrix setColumn(int col, double[] val) {
        if (val.length != rows) {
            System.err.println(
                    "ERROR: cannot set a column, the length of val[] must be equal "
                            + rows);
            return null;
        }

        if (col > cols - 1 || cols < 0) {
            System.err.println(
                    "ERROR: cannot set a column, condition: 0 <= index <= "
                            + (cols - 1));
            return null;
        }

        for (int i = 0; i < rows; i++) {
            matrix[i][col] = val[i];
        }

        return this;
    }

    public Matrix setRow(int row, double[] val) {
        if (val.length != cols) {
            System.err.println(
                    "ERROR: cannot set a row, the length of val[] must be equal "
                            + cols);
            return null;
        }

        if (row > rows - 1 || row < 0) {
            System.err.println(
                    "ERROR: cannot set a row, condition: 0 <= index <= " + (rows
                            - 1));
            return null;
        }

        for (int i = 0; i < cols; i++) {
            matrix[row][i] = val[i];
        }

        return this;
    }

    public void write2File(String path, boolean append) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file, append);
        writer.write(rows + MARK + cols + "\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                writer.write(matrix[i][j] + " ");
            }
            writer.write("\n");
        }
        writer.write("\n");
        writer.close();
    }

    public Matrix readFromFile(String path, boolean replaceThisMatrix)
            throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        String[] size = scanner.next().split(MARK);
        int rows = Integer.parseInt(size[0]);
        int cols = Integer.parseInt(size[1]);

        double[][] temp = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = scanner.nextDouble();
            }
        }
        scanner.close();

        if (replaceThisMatrix) {
            this.matrix = temp;
            this.rows = rows;
            this.cols = cols;
            return this;
        }

        return new Matrix(temp);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                builder.append(matrix[i][j]).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    @Override
    public Matrix add(double val) {
        double[][] temp = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i][j] + val;
            }
        }

        return new Matrix(temp);
    }

    @Override
    public Matrix add(Matrix m) {
        if (m.getCols() != cols || m.getRows() != rows) {
            System.err.println(
                    "ERROR: cannot add the matrix, conditions: cols = " + cols
                            + ", rows = " + rows);
            return null;
        }

        double[][] temp = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i][j] + m.getMatrix()[i][j];
            }
        }

        return new Matrix(temp);
    }

    @Override
    public Matrix multiple(double d) {
        double[][] temp = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i][j] * d;
            }
        }
        return new Matrix(temp);
    }

    @Override
    public Matrix multiple(Matrix m) {
        if (cols != m.getRows()) {
            System.err.printf(
                    "ERROR: the size of matrices are not matched, this.matrix's size %s, while parameter has size %s",
                    rows + "x" + cols, m.getRows() + "x" + m.getCols());
            return null;
        }

        double temp[][] = new double[rows][m.getCols()];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < m.getCols(); j++) {
                double sum = 0.0;
                for (int k = 0; k < cols; k++) {
                    sum += matrix[i][k] * m.getMatrix()[k][j];
                }
                temp[i][j] = sum;
            }
        }

        return new Matrix(temp);
    }

    @Override
    public double dotProduct(Matrix m) {
        if (rows > 1 && cols > 1) {
            System.err.println(
                    "ERROR: you should use 'Matrix dotProduct(Matrix m, boolean byColumn)'");
            return 0.0;
        }

        if (m.getRows() != rows || m.getCols() != cols) {
            System.err.println(
                    "ERROR: cannot dot product, 2 matrices must have same size, rows = "
                            + rows + ", cols = " + cols);
            return 0.0;
        }

        double dotResult = 0;
        if (rows == 1) {
            for (int i = 0; i < cols; i++) {
                dotResult += matrix[0][i] * m.getMatrix()[0][i];
            }
        } else if (cols == 1) {
            for (int i = 0; i < rows; i++) {
                dotResult += matrix[i][0] * m.getMatrix()[i][0];
            }
        }

        return dotResult;
    }

    @Override
    public Matrix dotProduct(Matrix m, boolean byColumn) {
        if (rows == 1 || cols == 1) {
            System.err.println(
                    "ERROR: you should use 'double dotProduct(Matrix m)'");
            return null;
        }

        if (m.getRows() != rows || m.getCols() != cols) {
            System.err.println(
                    "ERROR: cannot dot product, 2 matrices must have same size, rows = "
                            + rows + ", cols = " + cols);
            return null;
        }

        double[][] temp = null;
        if (byColumn) {
            temp = new double[1][cols];
            for (int j = 0; j < cols; j++) {
                double dotResult = 0.0;
                for (int i = 0; i < rows; i++) {
                    dotResult += matrix[i][j] * m.getMatrix()[i][j];
                }
                temp[0][j] = dotResult;
            }
        } else {
            temp = new double[rows][1];
            for (int i = 0; i < rows; i++) {
                double dotResult = 0.0;
                for (int j = 0; j < cols; j++) {
                    dotResult += matrix[i][j] * m.getMatrix()[i][j];
                }
                temp[i][0] = dotResult;
            }
        }

        return new Matrix(temp);
    }

    @Override
    public Matrix substract(Matrix m) {
        if (m.getCols() != cols || m.getRows() != rows) {
            System.err.println(
                    "ERROR: cannot substract the matrix, conditions: cols = "
                            + cols + ", rows = " + rows);
            return null;
        }

        double[][] temp = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i][j] - m.getMatrix()[i][j];
            }
        }

        return new Matrix(temp);
    }

    @Override
    public Matrix transpose() {
        double[][] temp = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[j][i] = matrix[i][j];
            }
        }

        return new Matrix(temp);
    }

    @Override
    public Matrix sum(boolean byColumn) {
        double[][] temp = null;
        if (byColumn) {
            temp = new double[rows][1];
            for (int i = 0; i < rows; i++) {
                double s = 0;
                for (int j = 0; j < cols; j++) {
                    s += matrix[i][j];
                }
                temp[i][0] = s;
            }
        } else {
            temp = new double[1][cols];
            for (int j = 0; j < cols; j++) {
                double s = 0;
                for (int i = 0; i < rows; i++) {
                    s += matrix[i][j];
                }
                temp[0][j] = s;
            }
        }

        return new Matrix(temp);
    }

    @Override
    public double sum() {
        if (rows > 1 && cols > 1) {
            System.err.println(
                    "ERROR: should use function 'Matrix sum(boolean byColumn)', rows ="
                            + rows + ", cols = " + cols);
            return 0.0;
        }

        double sum = 0;

        if (rows == 1) {
            for (int j = 0; j < cols; j++) {
                sum += matrix[0][j];
            }
        } else if (cols == 1) {
            for (int i = 0; i < rows; i++) {
                sum += matrix[i][0];
            }
        }

        return sum;
    }

    @Override
    public Matrix wiseMultiple(Matrix m) {
        if (m.getRows() != rows || m.getCols() != cols) {
            System.err.println(
                    "ERROR: cannot wise multiple, 2 matrices must have same size, rows = "
                            + rows + ", cols = " + cols);
            return null;
        }

        double[][] temp = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = m.getMatrix()[i][j] * matrix[i][j];
            }
        }
        return new Matrix(temp);
    }

    @Override
    public Matrix wiseSquare() {
        double[][] temp = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i][j] * matrix[i][j];
            }
        }
        return new Matrix(temp);
    }

    @Override
    public Matrix clone() {
        double[][] temp = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                temp[i][j] = matrix[i][j];
            }
        }

        return new Matrix(temp);
    }

    public double[] getRowAsArray(int row) {
        double[] temp = new double[cols];
        for (int i = 0; i < cols; i++) {
            temp[i] = matrix[row][i];
        }
        return temp;
    }

    public double[] getColumnAsArray(int col) {
        double[] temp = new double[rows];
        for (int i = 0; i < rows; i++) {
            temp[i] = matrix[i][col];
        }
        return temp;
    }

    public void clearMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = 0;
            }
        }
    }

}
