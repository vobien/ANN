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

    private String inputPath;
    private String outputPath;
    // private final String inputFeatureScalePath = "featureScale.txt";

    public Backpropagation(FeedForwardNetwork network, double learningRate,
            double momentum, String inputPath, String outputPath) {
        this.network = network;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.inputPath = inputPath;
        this.outputPath = outputPath;

        initBackwardNet();
    }

    private void initBackwardNet() {
        try {
            expectedMat = new Matrix();
            expectedMat.readFromFile(outputPath, true);
            network.setExpected(expectedMat);

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
        // x = (x-average of value range)/(maximum value) -0.5 <= x <= 0.5
        Matrix inputMat = new Matrix();
        inputMat.readFromFile(inputPath, true);

        // forward layers to get output
        network.forwardOuput(inputMat);
        // network.print2File("net_0" + i + ".txt");

        // backward layers to get error of each layer
        backwardNet.backward();
        // backwardNet.print2File("net_" + i + ".txt");

        // update weights of each layer
        backwardNet.updateWeight();

        // calculate global error after update weights
        Matrix inputTestMat = new Matrix();
        inputTestMat.readFromFile(inputPath, true);
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
