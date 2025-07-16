package engine;

/**
 * The Neuron class represents a single neuron in a neural network.
 */
public class Neuron {

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
        if (inputData.length != parameterSize) {
            throw new IllegalArgumentException("Input data size must match parameter size.");
        }

        double activation = bias; // Start with bias
        for (int i = 0; i < parameterSize; i++) {
            activation += weights[i] * inputData[i]; // Weighted sum
        }

        // Apply activation function (sigmoid)
        return 1 / (1 + Math.exp(-activation));
    }

    // TODO: Add method that applies deltas to the tuning values (weights and bias)

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
