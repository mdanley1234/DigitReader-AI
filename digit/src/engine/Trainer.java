package engine;

/**
 * The trainer class is responsible for training the neural network.
 * It will automatically pass necessary information to the
 */
public class Trainer {

    // The learning rate of the trainer
    public static final double LEARNING_RATE = 0.1;

    // The network to be trained
    private Network network;

    public Trainer(Network network) {
        this.network = network;
    }

    // Main method used to train the neural network using a Token
    // Returns the "cost" which measures the inaccuracy of the network
    public void trainNetwork(Token token) {

        // Check that the token is a training token
        if (!token.isTrainingToken()) {
            throw new IllegalArgumentException("Token is not a training token.");
        }

        // Process a token
        double[] outputData = network.processToken(token);
        double[] expectationData = token.getExpectationData();

        // Check that the token and network share the same output size
        if (expectationData.length != outputData.length) {
            throw new IllegalArgumentException("Expected token expectation data to be length " + outputData.length + " but was " + expectationData.length);
        }

        double[] costData = new double[outputData.length];

        // Calculate the cost matrix
        for (int i = 0; i < outputData.length; i++) {
            costData[i] = outputData[i] - expectationData[i];
        }

        network.backpropagate(costData);
    }

    // Returns the "cost" (inaccuracy) of the neural network in solving token data
    public double costFunction(Token token) {
        double[] outputData = network.processToken(token);
        double[] expectationData = token.getExpectationData();

        // Check that the token and network share the same output size
        if (expectationData.length != outputData.length) {
            throw new IllegalArgumentException("Expected token expectation data to be length " + outputData.length + " but was " + expectationData.length);
        }

        double errorSum = 0;
        for (int i = 0; i < outputData.length; i++) {
            errorSum += Math.pow((outputData[i] - expectationData[i]), 2);
        }

        return errorSum / 2.0;
    }

}
