package vn.tribt.ai.ann.error;

import vn.tribt.ai.ann.libs.Matrix;

public class ErrorCalculation {
    private double globalErrorSquare;
    private int trainingSize;

    public ErrorCalculation() {
    }

    public void reset() {
        globalErrorSquare = 0;
        trainingSize = 0;
    }

    public double calcRootMeanSquare() {
        return Math.sqrt(globalErrorSquare / trainingSize);
    }

    // TODO: check output layer if it has more than 1 neural
    public void updateGlobalError(Matrix actual, Matrix expected) {
        trainingSize = expected.getRows();
        globalErrorSquare = expected.substract(actual).wiseSquare().sum(true)
                .sum();
    }

}
