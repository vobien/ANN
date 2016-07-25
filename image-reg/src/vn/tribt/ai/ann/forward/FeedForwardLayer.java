package vn.tribt.ai.ann.forward;

import vn.tribt.ai.ann.activation.Activation;
import vn.tribt.ai.ann.libs.Matrix;

/**
 * each layer only has input matrix, no output matrix <br>
 * because output of a layer is set as the input of the next layer
 *
 * @author CEO_VoBien
 */
public class FeedForwardLayer {

    private Matrix input; // the LAST COLUMN is bias value
    private Matrix weight; // the LAST ROW is bias weight
    private Activation activation;

    private FeedForwardLayer next;
    private FeedForwardLayer prev;

    private int neuralNum;

    public FeedForwardLayer(int neuralNum) {
        this.neuralNum = neuralNum;
    }

    /**
     * calculate output for a layer and pass result to the next layer
     */
    public void calcForward() {
        addBias2Input();
        Matrix out = input.multiple(weight);
        activation.activate(out);
        this.next.setInput(out);
    }

    private void addBias2Input() {
        input.addColumn(input.getCols(), 1);
    }

    public Matrix getInput() {
        return input;
    }

    public void setInput(Matrix input) {
        this.input = input;
    }

    public Matrix getWeight() {
        return weight;
    }

    public void setWeight(Matrix weight) {
        this.weight = weight;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public FeedForwardLayer getNext() {
        return next;
    }

    public void setNext(FeedForwardLayer next) {
        this.next = next;
    }

    public FeedForwardLayer getPrev() {
        return prev;
    }

    public void setPrev(FeedForwardLayer prev) {
        this.prev = prev;
    }

    public int getNeuralNum() {
        return neuralNum;
    }

    public void setNeuralNum(int neuralNum) {
        this.neuralNum = neuralNum;
    }

    @Override
    public String toString() {
        return "FeedForwardLayer [\nneuralNum=" + neuralNum + "\nweight=\n"
                + weight + "Input=\n" + input + "]\n";
    }

    public FeedForwardLayer cloneLayer() {
        FeedForwardLayer layer = new FeedForwardLayer(neuralNum);
        layer.setWeight(weight);

        if (this.next != null) {
            layer.setInput(input.removeColumn(input.getCols() - 1));
        } else {
            layer.setInput(input);
        }

        return layer;
    }

}
