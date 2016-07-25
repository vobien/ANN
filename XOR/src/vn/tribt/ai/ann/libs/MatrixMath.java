package vn.tribt.ai.ann.libs;

public interface MatrixMath {
    Matrix add(Matrix m);

    Matrix add(double val);

    Matrix substract(Matrix m);

    Matrix multiple(double d);

    Matrix multiple(Matrix m);

    Matrix dotProduct(Matrix m, boolean byColumn);

    double dotProduct(Matrix m);

    Matrix wiseMultiple(Matrix m);

    Matrix wiseSquare();

    Matrix transpose();

    Matrix sum(boolean byColumn);

    double sum();

}
