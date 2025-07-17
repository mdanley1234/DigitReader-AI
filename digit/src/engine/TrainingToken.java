package engine;

/**
 * Contains token data and expected output layer activation data for training purposes
 */
public class TrainingToken {
    
    // Token data and expected activation arrays
    private double[] tokenData; // Data to be fed into the Trainer
    private double[] activationExpectation; // Expected output of network used by Trainer to calculate the cost function
    
    public TrainingToken(double[] tokenData, double[] activationExpectation) {
        this.tokenData = tokenData;
        this.activationExpectation = activationExpectation;
    }

    public double[] getTokenData() {
        return tokenData;
    }

    public double[] getActivationExpectation() {
        return activationExpectation;
    }

}
