package vn.tribt.ai.ann.backward;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import vn.tribt.ai.ann.forward.FeedForwardLayer;
import vn.tribt.ai.ann.libs.Matrix;

public class BackwardNetwork {

    private BackwardLayer first;
    private BackwardLayer last;
    private double learningRate;
    private double momentum;
    private Matrix expectedMat;

    public BackwardNetwork(Matrix expectedMat, double learningRate,
            double momentum) {
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.expectedMat = expectedMat;
    }

    public void add(FeedForwardLayer forwardLayer) {
        BackwardLayer layer = new BackwardLayer(forwardLayer);

        if (first == null) {
            first = layer;
            last = layer;
            return;
        }

        layer.setPrev(last);
        last.setNext(layer);
        last = layer;
    }

    public void backward() {
        BackwardLayer current = first;
        while (current != null) {
            current.calcError(expectedMat);
            current = current.getNext();
        }
    }

    public void updateWeight() {
        BackwardLayer current = first.getNext();
        while (current != null) {
            current.updateWeight(learningRate, momentum);
            current = current.getNext();
        }
    }

    public BackwardLayer getFirst() {
        return first;
    }

    public void setFirst(BackwardLayer first) {
        this.first = first;
    }

    public BackwardLayer getLast() {
        return last;
    }

    public void setLast(BackwardLayer last) {
        this.last = last;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public void print2File(String path) throws IOException {
        FileWriter writer = new FileWriter(new File(path));
        BackwardLayer current = first;
        while (current != null) {
            writer.write(current.toString());
            current = current.getNext();
        }

        writer.close();
    }

}
