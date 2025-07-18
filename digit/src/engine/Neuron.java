package engine;

/**
 * The Neuron class represents a single neuron in a neural network.
 */
public class Neuron {

    // Store last activation value for backpropagation
    private double activationValue = 0.0;

    // Store input token data for the neuron to calculate weight adjustments
    private double[] inputData;

    // Number of weight parameters for the neuron
    private final int parameterSize;

    // Tuning values (weights and bias)
    private double[] weights;
    private double bias;
    
    // Build neuron using CSV data
    public Neuron(double[] CSVData) {
        parameterSize = CSVData.length - 1; // Last value is bias
        weights = new double[parameterSize];

        // Initialize weights and bias from CSV data
        System.arraycopy(CSVData, 0, weights, 0, parameterSize);
        bias = CSVData[parameterSize];
    }

    // Calculate neuron activation value (0,1) from input data
    public double calculateActivation(double[] inputData) {
        this.inputData = inputData;
        if (inputData.length != parameterSize) {
            throw new IllegalArgumentException("Input data size must match parameter size.");
        }

        double activation = bias; // Start with bias
        for (int i = 0; i < parameterSize; i++) {
            activation += weights[i] * inputData[i]; // Weighted sum
        }

        // Apply activation function (sigmoid)
        activationValue = 1 / (1 + Math.exp(-activation));
        return activationValue;
    }

    // TODO: Add method that applies deltas to the tuning values (weights and bias)

    // Calculate the error delta for the neuron and returns an array of (errorDelta * weights)
    // Parameter requires the derivative of the cost function from the previous layer
    // For output layer use the (cost matrix), for hidden layers use sum (errorDelta * weights)
    public double[] calculateDelta(double derivativeInput) {
        double delta = activationValue * (1 - activationValue) * derivativeInput; // Sigmoid derivative

        // Multiply by weights and return
        double[] deltaWeights = new double[parameterSize];
        for (int i = 0; i < parameterSize; i++) {
            deltaWeights[i] = delta * weights[i];
        }

        // Update weights and bias
        for (int i = 0; i < parameterSize; i++) {
            weights[i] -= delta * inputData[i] * Trainer.LEARNING_RATE; // Update weights
        }
        bias -= delta * Trainer.LEARNING_RATE; // Update bias

        return deltaWeights;
    }

    // Tuning values setter/getter methods
    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        if (weights.length != parameterSize) {
            throw new IllegalArgumentException("Weights array size must match input size.");
        }
        this.weights = weights;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
