package vn.tribt.ai.ann.training;

import java.io.FileNotFoundException;
import java.io.IOException;

import vn.tribt.ai.ann.backward.BackwardLayer;
import vn.tribt.ai.ann.backward.BackwardNetwork;
import vn.tribt.ai.ann.forward.FeedForwardLayer;
import vn.tribt.ai.ann.forward.FeedForwardNetwork;
import vn.tribt.ai.ann.libs.Matrix;

public class Backpropagation {
    private FeedForwardNetwork network;
    private BackwardNetwork backwardNet;
    private double learningRate;
    private double momentum;
    private Matrix expectedMat;
    private double globalError;

    public static final String INPUT_PATH = "input.txt";
    public static final String EXPECTED_PATH = "expectedOutput.txt";

    public Backpropagation(FeedForwardNetwork network, double learningRate,
            double momentum) {
        this.network = network;
        this.learningRate = learningRate;
        this.momentum = momentum;

        initBackwardNet();
    }

    private void initBackwardNet() {
        try {
            expectedMat = new Matrix();
            expectedMat.readFromFile(EXPECTED_PATH, true);

            backwardNet = new BackwardNetwork(expectedMat, learningRate,
                    momentum);
            FeedForwardLayer forwardLayer = network.getOutLayer();
            while (forwardLayer != null) {
                backwardNet.add(forwardLayer);
                forwardLayer = forwardLayer.getPrev();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void iteration(int i) throws IOException {
        // load input matrix from file
        Matrix inputMat = new Matrix();
        inputMat.readFromFile(INPUT_PATH, true);

        // forward layers to get output
        network.forwardOuput(inputMat);

        // backward layers to get error of each layer
        backwardNet.backward();
        // backwardNet.print2File("net_" + i + ".txt");

        // update weights of each layer
        backwardNet.updateWeight();

        // calculate global error after update weights
        Matrix inputTestMat = new Matrix();
        inputTestMat.readFromFile(INPUT_PATH, true);
        globalError = network.calculationGlobalError(inputTestMat, expectedMat);

        // clear error and accumulateMatrix
        clearError();
    }

    private void clearError() {
        BackwardLayer backlayer = backwardNet.getFirst();
        while (backlayer != null) {
            backlayer.clearError();
            backlayer = backlayer.getNext();
        }
    }

    public double getGlobalError() {
        return globalError;
    }

    public Matrix getExpectedMat() {
        return expectedMat;
    }

    public void setExpectedMat(Matrix expectedMat) {
        this.expectedMat = expectedMat;
    }

}
