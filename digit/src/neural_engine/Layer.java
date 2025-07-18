package neural_engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * The Layer class represents a layer in a neural network, containing nodes and
 * their parameters.
 */
public class Layer {

    // Number of weight parameters for each node (number of tokens for initial layer; previous layerSize for subsequent layers)
    private int parameterSize;

    // Number of nodes in the layer (size of node array)
    private int layerSize;

    // Tuning values (weights and bias) file path
    private String layerFile;

    // Node array
    private Node[] nodes;

    // Initialize layer
    public Layer(int parameterSize, int layerSize, String layerFile) {
        this.parameterSize = parameterSize;
        this.layerSize = layerSize;
        this.layerFile = layerFile;
        nodes = new Node[layerSize];

        // Attempt to read the layer file; if it doesn't exist, generate a new file with random values
        try (BufferedReader br = new BufferedReader(new FileReader(layerFile))) {

            // Read the data from the file
            String line = br.readLine();
            int neuronIndex = 0;
            while (line != null) {

                // Extract CSV data line
                String[] values = line.split(",");

                // Check for valid data formatting
                if (values.length != parameterSize + 1) {
                    throw new NumberFormatException("Invalid CSV data line length (line " + (neuronIndex + 1) + "). Expected " + (parameterSize + 1) + " values, but got " + values.length);
                }

                // Convert values to double array
                double[] CSVData = new double[parameterSize + 1];
                for (int i = 0; i < parameterSize + 1; i++) {
                    CSVData[i] = Double.parseDouble(values[i]);
                }

                // Create and add node using CSV data
                nodes[neuronIndex] = new Node(CSVData);

                line = br.readLine();
                neuronIndex++;
            }

            // Check if the number of neurons read matches the expected layer size
            if (neuronIndex != layerSize) {
                throw new NumberFormatException("Layer file contains " + neuronIndex + " neurons, expected " + layerSize + ".");
            }
        } catch (IOException e) {

            // If the file does not exist, create a new one with random values
            try {
                File file = new File(layerFile);
                if (!file.exists()) {
                    // Create a new file and use random values (-1,1))
                    file.createNewFile();
                    Random rand = new Random();

                    // Write random values to the file
                    try (FileWriter fw = new FileWriter(file)) {
                        for (int i = 0; i < layerSize; i++) {
                            StringBuilder sb = new StringBuilder(); // For file initialization
                            double[] CSVData = new double[parameterSize + 1]; // For neuron initialization
                            for (int j = 0; j < parameterSize + 1; j++) {
                                double value = -1 + 2 * rand.nextDouble(); // RAND BOUND: [-1, 1)
                                CSVData[j] = value; // Store value for neuron initialization
                                sb.append(value);
                                if (j < parameterSize) {
                                    sb.append(",");
                                }
                            }
                            sb.append("\n");
                            fw.write(sb.toString());

                            // Create neuron using CSV data
                            nodes[i] = new Node(CSVData);
                        }
                    }
                }
            } catch (IOException ex) {
                // Failure to create layer file
                System.err.println("Error creating layer file: " + ex.getMessage());
                System.exit(1); // Failure to create layer file
            }
        } catch (NumberFormatException e) {
            // End program with error message if the file is not formatted as expected
            System.err.println("Error reading layer file: " + e.getMessage());
            System.exit(1); // Layer file format error
        }
    }

    // Calculate and return activation values for all nodes in the layer
    public double[] calculateLayerActivation(double[] inputData) {
        // Check that input data size matches parameter size
        if (inputData.length != parameterSize) {
            throw new IllegalArgumentException("Input data size must match parameter size.");
        }

        // Calculate activation for each node
        double[] activations = new double[layerSize];
        for (int i = 0; i < layerSize; i++) {
            activations[i] = nodes[i].calculateActivation(inputData);
        }

        return activations;
    }

    // Save node tuning values to the layer file
    public void saveLayer() {
        try (FileWriter writer = new FileWriter(layerFile, false)) {

            // Write each neuron's weights and bias as a CSV data line in file
            for (Node neuron : nodes) {
                // Build CSV data line
                StringBuilder sb = new StringBuilder();
                double[] weights = neuron.getWeights();
                for (int i = 0; i < parameterSize; i++) {
                    sb.append(weights[i]);
                    sb.append(",");
                }
                sb.append(neuron.getBias()).append("\n"); // Append bias at the end
                writer.write(sb.toString()); // Write CSV data line to file
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Train the layer using a matrix of trainingDerivatives
     *
     * @param derivativeMatrix the matrix of training derivatives for the layer
     * @return
     */
    public double[] trainLayer(double[] derivativeMatrix) {
        // Check that the derivative matrix size matches the layer size
        if (derivativeMatrix.length != layerSize) {
            throw new IllegalArgumentException("Cost matrix size must match layer size.");
        }

        // List of training value matricies to be used for derivative calculations for the next layer
        ArrayList<double[]> trainingValues = new ArrayList<>();

        // Extract list of training value matricies from the layer nodes
        for (int i = 0; i < layerSize; i++) {
            Node neuron = nodes[i];
            double[] deltaWeights = neuron.trainNode(derivativeMatrix[i]);
            trainingValues.add(deltaWeights);
        }

        // Calculate the new derivatives for the next layer
        double[] newDerivatives = new double[parameterSize];
        for (int i = 0; i < parameterSize; i++) {
            // Calculate deltaDerivative information for the next layer
            double deltaSum = 0.0;
            for (double[] deltaArray : trainingValues) {
                deltaSum += deltaArray[i];
            }
            newDerivatives[i] = deltaSum;
        }

        // Return the new derivatives for the next layers backpropagation calculation
        return newDerivatives;
    }

    // Getters
    public int getParameterSize() {
        return parameterSize;
    }

    public int getLayerSize() {
        return layerSize;
    }

}
