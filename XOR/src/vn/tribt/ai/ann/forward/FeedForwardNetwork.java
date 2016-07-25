package vn.tribt.ai.ann.forward;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import vn.tribt.ai.ann.activation.Activation;
import vn.tribt.ai.ann.error.ErrorCalculation;
import vn.tribt.ai.ann.libs.Matrix;

public class FeedForwardNetwork {

    private FeedForwardLayer inLayer;
    private FeedForwardLayer outLayer;

    private ErrorCalculation errorCalculation;
    private Activation activation;

    public FeedForwardNetwork(ErrorCalculation errorCalculation,
            Activation activation) {
        this.errorCalculation = errorCalculation;
        this.activation = activation;
    }

    public void forwardOuput(Matrix input) {
        FeedForwardLayer current = inLayer;
        inLayer.setInput(input);

        while (current.getNext() != null) {
            current.calcForward();
            current = current.getNext();
        }
    }

    /**
     * after update weight, calculate output of network
     * with input (feed forward), then calculate error
     *
     */
    public double calculationGlobalError(Matrix input, Matrix expected) {
        forwardOuput(input);
        errorCalculation.updateGlobalError(outLayer.getInput(), expected);
        return errorCalculation.calcRootMeanSquare();
    }

    public void addLayer(FeedForwardLayer layer) {
        layer.setActivation(activation);

        if (inLayer == null) {
            inLayer = layer;
            outLayer = layer;
            return;
        }

        outLayer.setNext(layer);
        layer.setPrev(outLayer);
        outLayer = layer;
    }

    /**
     * weight of bias is the LAST ROW of the weight matrix
     */
    public void initWeightMatrix() {
        FeedForwardLayer current = inLayer;
        while (current.getNext() != null) {
            Matrix weightMat = new Matrix(1 + current.getNeuralNum(), current
                    .getNext().getNeuralNum(), true);
            current.setWeight(weightMat);
            current = current.getNext();
        }
    }

    public void loadWeightMatrix() throws FileNotFoundException {
        FeedForwardLayer current = inLayer;
        int i = 0;
        while (current.getNext() != null) {
            Matrix weightMat = new Matrix();
            weightMat.readFromFile("weight_" + i + ".txt", true);
            current.setWeight(weightMat);
            current = current.getNext();
            i++;
        }
    }

    public void traverse() {
        FeedForwardLayer current = inLayer;
        while (current != null) {
            System.out.println(current);
            current = current.getNext();
        }
    }

    public void print2File(String path) throws IOException {
        FileWriter writer = new FileWriter(new File(path));
        FeedForwardLayer current = inLayer;
        while (current != null) {
            writer.write(current.toString());
            current = current.getNext();
        }

        writer.close();
    }

    public FeedForwardLayer getInLayer() {
        return inLayer;
    }

    public void setInLayer(FeedForwardLayer inLayer) {
        this.inLayer = inLayer;
    }

    public FeedForwardLayer getOutLayer() {
        return outLayer;
    }

    public void setOutLayer(FeedForwardLayer outLayer) {
        this.outLayer = outLayer;
    }

    public ErrorCalculation getErrorCalculation() {
        return errorCalculation;
    }

    public void setErrorCalculation(ErrorCalculation errorCalculation) {
        this.errorCalculation = errorCalculation;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

}
