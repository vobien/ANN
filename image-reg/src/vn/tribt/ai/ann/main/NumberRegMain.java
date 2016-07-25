package vn.tribt.ai.ann.main;

import java.io.File;
import java.io.FileWriter;
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
public class NumberRegMain {

    public static double LEARNING_RATE = 0.2;
    public static double MOMENTUM = 0.5;
    public static int ITERATION_NUM = 800;
    public static int LOOP_NUM = 10;

    public static double GLOBAL_ERROR = 0.1;

    public static final String INPUT_PATH = "featureScale.txt";
    public static final String OUTPUT_PATH = "output.txt";

    // input_layer_size = 35; % 7x5 Input Images of Digits
    // hidden_layer_size = ?
    // output_layer_size = 10; % bring binary value from 0 to 9
    public static final int INPUT_LAYER_SIZE = 35;
    public static final int HIDDEN_LAYER_SIZE_1 = 10;
    // public static final int HIDDEN_LAYER_SIZE_2 = 10;
    public static final int OUTPUT_LAYER_SIZE = 10;

    // • The number of hidden neurons should be between the size of the input
    // layer and the size of the output layer.
    // • The number of hidden neurons should be 2/3 the size of the input layer,
    // plus the size of the output layer.
    // • The number of hidden neurons should be less than twice the size of the
    // input layer.
    public static void main(String[] args) throws IOException {
        ErrorCalculation errorCalc = new ErrorCalculation();
        Activation activation = new Activation();

        FeedForwardNetwork network = new FeedForwardNetwork(errorCalc,
                activation);
        network.addLayer(new FeedForwardLayer(INPUT_LAYER_SIZE));
        network.addLayer(new FeedForwardLayer(HIDDEN_LAYER_SIZE_1));
        // network.addLayer(new FeedForwardLayer(HIDDEN_LAYER_SIZE_2));
        network.addLayer(new FeedForwardLayer(OUTPUT_LAYER_SIZE));
        network.initWeightMatrix();

        // load weight matrix from file
        // network.loadWeightMatrix();

        Backpropagation backpropagation = new Backpropagation(network,
                LEARNING_RATE, MOMENTUM, INPUT_PATH, OUTPUT_PATH);

        int loop = 1;
        double globalErr = 0.0;
        double minError = 100.0;
        FeedForwardNetwork finalNetwork = null;
        do {
            globalErr = doIteration(network, backpropagation);
            if (minError > globalErr) {
                minError = globalErr;
                finalNetwork = network.cloneNet();
            }
            loop++;

        } while (globalErr > GLOBAL_ERROR && loop < LOOP_NUM);

        // only print the network if training is successful
        if (loop < LOOP_NUM) {
            System.err.println("Training is sucess after loop " + loop
                    + " times");
            finalNetwork.print2File("successTraining.txt");
            printData2File("successTraining.txt", "Global Error = " + minError);
        } else {
            System.err.println("Training is not good but we got min error: "
                    + minError);
            finalNetwork.print2File("minErrorNetwork.txt");
            printData2File("minErrorNetwork.txt", "Global Error = " + minError);
        }

        // calculate accuracy
        double accuracy = finalNetwork.evaluateAccuracy();
        System.out.println("Accuracy = " + accuracy + "%");
        if (loop < LOOP_NUM) {
            printData2File("successTraining.txt", "Accuracy = " + accuracy
                    + "%");
        } else {
            printData2File("minErrorNetwork.txt", "Accuracy = " + accuracy
                    + "%");
        }

    }

    public static double doIteration(FeedForwardNetwork network,
            Backpropagation backpropagation) throws IOException {

        // initialize weight matrix for 1 loop
        network.initWeightMatrix();

        double globalError = 0.0;
        int i = 1;
        do {
            backpropagation.iteration(i);
            globalError = backpropagation.getGlobalError();
            System.out.println("Iteration " + i + ", Error = " + globalError);
            i++;

            // System.out.println("Actual output: \n" + network.getOutLayer()
            // .getInput() + "Expected output: \n" + backpropagation
            // .getExpectedMat());

        } while (globalError > GLOBAL_ERROR && i <= ITERATION_NUM);

        // System.out.println();
        // System.err.println("--> TRAINING IS DONE - Iteration: " + i
        // + ", Error: " + globalError);
        //
        // System.out.println("Actual output: \n" + network.getOutLayer()
        // .getInput() + "Expected output: \n" + backpropagation
        // .getExpectedMat());

        return globalError;
    }

    public static void printData2File(String path, String data)
            throws IOException {
        FileWriter writer = new FileWriter(new File(path), true);
        writer.append("\n").append(data);
        writer.close();
    }
}
