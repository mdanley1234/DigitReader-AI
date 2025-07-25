package neural_engine;

import javax.naming.directory.InvalidAttributesException;

/**
 * A token is the most basic data element of the neural network. It stores
 * the data array to be fed into the network from raw data. All values must be
 * scaled between [0,1] for the Token to be valid. Token also contains an
 * optional expectation parameter for training purposes. It must also be scaled
 * between [0,1] for the Token to be valid.
 */
public class Token {

    // Token data and expectation data
    protected double[] tokenData;
    protected double[] expectationData;
    protected boolean trainingToken = false;

    // Constructor for training purposes
    public Token(double[] tokenData, double[] expectationData) {
        this.tokenData = tokenData;
        this.expectationData = expectationData;
        trainingToken = true;
    }

    // Constructor for general token purposes
    public Token(double[] tokenData) {
        this.tokenData = tokenData;
    }

    // Getters

    public double[] getTokenData() {
        return tokenData;
    }

    public double[] getExpectationData() {
        try {
            // Check for training token
            if (!trainingToken) {
                throw new InvalidAttributesException("Token is not designated for training purposes.");
            }
        } catch (InvalidAttributesException e) {
            System.exit(1); // Invalid token type
        }

        // If valid training token
        return expectationData;
    }

    public boolean isTrainingToken() {
        return trainingToken;
    }

}
