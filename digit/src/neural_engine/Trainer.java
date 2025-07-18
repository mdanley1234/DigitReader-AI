package neural_engine;

import java.util.ArrayList;

/**
 * The trainer class is responsible for training the neural network.
 */
public class Trainer {

    // The learning rate of the trainer
    public static double LEARNING_RATE = 0.01;

    // The network to be trained
    private Network network;

    public Trainer(Network network) {
        this.network = network;
    }

    // Train the network with a list of tokens
    public void trainNetwork(ArrayList<Token> trainingTokens) {
        // Iterate through each token in the training set
        for (Token token : trainingTokens) {
            trainNetwork(token);
        }
        network.saveNetwork();
    }

    // Trains the network with a given token
    public void trainNetwork(Token token) {

        // Check that the token is a training token
        if (!token.isTrainingToken()) {
            throw new IllegalArgumentException("Token is not a training token.");
        }

        // Process token
        double[] outputData = network.processToken(token);
        double[] expectationData = token.getExpectationData();

        // Check that the token and network share the same output size
        if (expectationData.length != outputData.length) {
            throw new IllegalArgumentException("Expected token expectation data to be length " + outputData.length + " but was " + expectationData.length);
        }

        // Calculate the cost matrix
        double[] costMatrix = new double[outputData.length];
        for (int i = 0; i < outputData.length; i++) {
            costMatrix[i] = outputData[i] - expectationData[i];
        }

        network.trainNetwork(costMatrix);
    }
}
