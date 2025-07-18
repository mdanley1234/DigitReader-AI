package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * A layer consists of a collection of neurons.
 */
public class Layer {

    // Number of weight parameters for each neuron (number of tokens for initial layer; previous layerSize for subsequent layers)
    private int parameterSize;

    // Number of neurons in the layer (size of neuron array)
    private int layerSize;

    // Tuning values (weights and bias) file path
    private String layerFile;

    // Neuron array
    private Neuron[] neurons;

    // Initialize layer
    public Layer(int parameterSize, int layerSize, String layerFile) {
        this.parameterSize = parameterSize;
        this.layerSize = layerSize;
        this.layerFile = layerFile;
        neurons = new Neuron[layerSize];

        // Attempt to read the layer file; if it doesn't exist, generate a new file with random values
        try (BufferedReader br = new BufferedReader(new FileReader(layerFile))) {

            // Read the data from the file
            String line = br.readLine();
            int neuronIndex = 0;
            while (line != null) {

                // Extract CSV data line
                String[] values = line.split(",");

                // Check for valid data line length
                if (values.length != parameterSize + 1) {
                    throw new NumberFormatException("Invalid CSV data line length (line " + (neuronIndex + 1) + "). Expected " + (parameterSize + 1) + " values, but got " + values.length);
                }

                // Convert values to double array
                double[] CSVData = new double[parameterSize + 1];
                for (int i = 0; i < parameterSize + 1; i++) {
                    CSVData[i] = Double.parseDouble(values[i]);
                }

                // Create neuron using CSV data
                neurons[neuronIndex] = new Neuron(CSVData);

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

                            // Write to file
                            fw.write(sb.toString());

                            // Create neuron using CSV data
                            neurons[i] = new Neuron(CSVData);
                        }
                    }
                }
            } catch (IOException ex) {
                // Failure to create layer file
                System.err.println("Error creating layer file: " + ex.getMessage());
                ex.printStackTrace();
                System.exit(1); // Failure to create layer file
            }
        } catch (NumberFormatException e) {
            // End program with error message if the file is not formatted as expected
            System.err.println("Error reading layer file: " + e.getMessage());
            e.printStackTrace();
            System.exit(2); // Layer file format error
        }

    }

    // Calculate activation values for all neurons in the layer
    public double[] calculateActivationLayer(double[] inputData) {
        if (inputData.length != parameterSize) {
            throw new IllegalArgumentException("Input data size must match parameter size.");
        }

        double[] activations = new double[layerSize];

        // Calculate activation for each neuron
        for (int i = 0; i < layerSize; i++) {
            activations[i] = neurons[i].calculateActivation(inputData);
        }

        return activations;
    }

    // Save neuron tuning values to the layer file
    public void saveLayer() {
        try (FileWriter writer = new FileWriter(layerFile, false)) {

            // Write each neuron's weights and bias as a CSV data line in file
            for (Neuron neuron : neurons) {
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

    // Distribution manifold for derivatives 
    public ArrayList<double[]> calculateDerivatives(double[] derivativeMatrix) {
        if (derivativeMatrix.length != layerSize) {
            throw new IllegalArgumentException("Cost matrix size must match layer size.");
        }

        ArrayList<double[]> derivatives = new ArrayList<>();

        // Calculate derivatives for each neuron
        for (int i = 0; i < layerSize; i++) {
            Neuron neuron = neurons[i];
            double[] deltaWeights = neuron.calculateDelta(derivativeMatrix[i]);
            derivatives.add(deltaWeights);
        }

        return derivatives;
    }

    // Getters for layer properties

    public int getParameterSize() {
        return parameterSize;
    }

    public int getLayerSize() {
        return layerSize;
    }


    
}
