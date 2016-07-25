package vn.tribt.ai.ann.backward;

import vn.tribt.ai.ann.forward.FeedForwardLayer;
import vn.tribt.ai.ann.libs.Matrix;

public class BackwardLayer {
    private Matrix error;
    private Matrix errorDelta;
    private Matrix accumulateWeight;
    private FeedForwardLayer forwardLayer;

    private BackwardLayer next;
    private BackwardLayer prev;

    // used for momentum - avoid variance
    private Matrix matrixDelta;

    public BackwardLayer(FeedForwardLayer forwardLayer) {
        this.forwardLayer = forwardLayer;

        if (forwardLayer.getNext() != null) {
            matrixDelta = new Matrix(forwardLayer.getWeight(), false);
        }
    }

    /**
     * VERY IMPORTANT!!! <br>
     *
     * TRUE: errorOutLayer = expectedOut - actualOut <br>
     *
     * WRONG: errorOutLayer = actualOut - expectedOut <br>
     * -->because error is always increased
     */
    public void calcError(Matrix expectedMat) {
        // calculate error for output layer
        if (prev == null) {
            error = expectedMat.substract(forwardLayer.getInput());
            errorDelta = error.wiseMultiple(forwardLayer.getActivation()
                    .derivative(forwardLayer.getInput()));
            return;
        }

        // calculate error for hidden layer and input layer
        accumulateWeight = forwardLayer.getInput().removeColumn(forwardLayer
                .getInput().getCols() - 1).transpose().multiple(prev
                        .getErrorDelta());
        accumulateWeight.addRow(forwardLayer.getWeight().getRows() - 1, prev
                .getErrorDelta().sum(false).getRowAsArray(0));

        if (next != null) {
            error = prev.getErrorDelta().multiple(forwardLayer.getWeight()
                    .removeRow(forwardLayer.getWeight().getRows() - 1)
                    .transpose());

            errorDelta = error.wiseMultiple(forwardLayer.getActivation()
                    .derivative(forwardLayer.getInput().removeColumn(
                            forwardLayer.getInput().getCols() - 1)));
        }
    }

    public void updateWeight(double learningRate, double momentum) {
        Matrix m1 = matrixDelta.multiple(momentum);
        Matrix m2 = accumulateWeight.multiple(learningRate);
        matrixDelta = m2.add(m1);
        forwardLayer.setWeight(forwardLayer.getWeight().add(matrixDelta));
    }

    public Matrix getError() {
        return error;
    }

    public void setError(Matrix error) {
        this.error = error;
    }

    public Matrix getErrorDelta() {
        return errorDelta;
    }

    public void setErrorDelta(Matrix errorDelta) {
        this.errorDelta = errorDelta;
    }

    public Matrix getAccumulateWeight() {
        return accumulateWeight;
    }

    public void setAccumulateWeight(Matrix accumulateWeight) {
        this.accumulateWeight = accumulateWeight;
    }

    public FeedForwardLayer getForwardLayer() {
        return forwardLayer;
    }

    public void setForwardLayer(FeedForwardLayer forwardLayer) {
        this.forwardLayer = forwardLayer;
    }

    public BackwardLayer getNext() {
        return next;
    }

    public void setNext(BackwardLayer next) {
        this.next = next;
    }

    public BackwardLayer getPrev() {
        return prev;
    }

    public void setPrev(BackwardLayer prev) {
        this.prev = prev;
    }

    public void clearError() {
        if (error != null) {
            error.clearMatrix();
        }

        if (error != null) {
            errorDelta.clearMatrix();
        }

        if (accumulateWeight != null) {
            accumulateWeight.clearMatrix();
        }
    }

    @Override
    public String toString() {
        return "BackwardLayer [error=\n" + error + ", errorDelta=\n"
                + errorDelta + ", accumulateWeight=\n" + accumulateWeight
                + ", forwardLayer=" + forwardLayer + "]";
    }

}
