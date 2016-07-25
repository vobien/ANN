package vn.tribt.ai.ann.main;

import java.io.IOException;

import vn.tribt.ai.ann.activation.Activation;
import vn.tribt.ai.ann.error.ErrorCalculation;
import vn.tribt.ai.ann.forward.FeedForwardLayer;
import vn.tribt.ai.ann.forward.FeedForwardNetwork;
import vn.tribt.ai.ann.training.Backpropagation;

/**
 * Remember: errorOutLayer = expectedOut - actualOut
 *
 * @author CEO_VoBien
 *
 */
public class XorMain {

    public static double LEARNING_RATE = 0.8;
    public static double MOMENTUM = 0.9;
    public static int ITERATION_NUM = 5000;

    public static double GLOBAL_ERROR = 0.01;

    public static final String INPUT_PATH = "input.txt";
    public static final String EXPECTED_PATH = "expectedOutput.txt";

    public static void main(String[] args) throws IOException {
        ErrorCalculation errorCalc = new ErrorCalculation();
        Activation activation = new Activation();

        FeedForwardNetwork network = new FeedForwardNetwork(errorCalc,
                activation);
        network.addLayer(new FeedForwardLayer(2));
        network.addLayer(new FeedForwardLayer(3));
        network.addLayer(new FeedForwardLayer(1));
        network.initWeightMatrix();

        // load weight matrix from file
        // network.loadWeightMatrix();

        Backpropagation backpropagation = new Backpropagation(network,
                LEARNING_RATE, MOMENTUM);

        int i = 1;
        do {
            backpropagation.iteration(i);
            System.out.println("Iteration " + i + ", Error =  "
                    + backpropagation.getGlobalError());
            i++;

            System.out.println("Actual output: \n" + network.getOutLayer()
                    .getInput() + "Expected output: \n" + backpropagation
                            .getExpectedMat());

        } while (backpropagation.getGlobalError() > GLOBAL_ERROR
                && i <= ITERATION_NUM);

        System.out.println();
        System.err.println("--- TRAINING IS DONE - Iteration: " + i + " ---");
        System.err.println("Error: " + backpropagation.getGlobalError());

    }
}
