package vn.tribt.ai.ann.activation;

import vn.tribt.ai.ann.libs.BoundNumber;
import vn.tribt.ai.ann.libs.Matrix;

public class Activation {

    public Activation() {

    }

    public double activate(double d) {
        return 1 / (1 + Math.exp(-1 * d));
    }

    public void activate(Matrix m) {
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                m.getMatrix()[i][j] = BoundNumber.adjust(1.0 / (1.0 + Math.exp(
                        -1.0 * m.getMatrix()[i][j])));
            }
        }
    }

    public Matrix derivative(Matrix m) {
        return m.wiseMultiple(m.multiple(-1).add(1));
    }
}
