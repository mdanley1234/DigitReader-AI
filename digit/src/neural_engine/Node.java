package neural_engine;

/**
 * The Node class represents a single node in the layer.
 */
public class Node {

    // Storage objects for activation value and input data for use in backpropagation training
    private double activationValue = 0.0;
    private double[] inputData;

    // Number of weight parameters for the node
    private final int parameterSize;

    // Tuning values (weights and bias)
    private double[] weights;
    private double bias;

    // Build node using CSV data
    public Node(double[] CSVData) {
        parameterSize = CSVData.length - 1; // Last value is bias
        weights = new double[parameterSize];

        // Initialize weights and bias from CSV data
        System.arraycopy(CSVData, 0, weights, 0, parameterSize);
        bias = CSVData[parameterSize];
    }

    // Calculate and return node activation value (0,1) from input data
    public double calculateActivation(double[] inputData) {
        this.inputData = inputData;

        // Check that input data size matches parameter size
        if (inputData.length != parameterSize) {
            throw new IllegalArgumentException("Input data size must match parameter size.");
        }

        // Perform weighted sum of inputs and bias
        double activation = bias; // Start with bias
        for (int i = 0; i < parameterSize; i++) {
            activation += weights[i] * inputData[i]; // Weighted sum
        }

        // Apply activation function (sigmoid)
        activationValue = 1 / (1 + Math.exp(-activation));
        return activationValue;
    }

    // Calculate the error delta for the neuron and returns an array of (errorDelta * weights)
    // Parameter requires the derivative of the cost function from the previous layer
    // For output layer use the (cost matrix), for hidden layers use sum (errorDelta * weights)
    /**
     * The trainNode method updates the weights and bias of the node based on
     * the trainingDerivative. A trainingDerivative parameter is used to
     * calculate the delta value for the node. This is combined with previously
     * processed token data to update the weights and bias.
     *
     * @param trainingDerivative
     * @return double[] containing values relevant to trainingDerivative
     * calculations for the next layer
     */
    public double[] trainNode(double trainingDerivative) {
        double delta = activationValue * (1 - activationValue) * trainingDerivative; // Sigmoid derivative

        // Calculate derivativeComponents for backpropagation in the next layer
        double[] derivativeComponents = new double[parameterSize];
        for (int i = 0; i < parameterSize; i++) {
            derivativeComponents[i] = delta * weights[i];
        }

        // Update weights and bias using the delta value, LEARNING_RATE, and previous token data
        for (int i = 0; i < parameterSize; i++) {
            weights[i] -= delta * inputData[i] * Trainer.LEARNING_RATE;
        }
        bias -= delta * Trainer.LEARNING_RATE;

        return derivativeComponents;
    }

    // Getters

    public double[] getWeights() {
        return weights;
    }

    public double getBias() {
        return bias;
    }
}
