package neural_engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Network class represents a neural network, containing multiple layers and
 * methods to process tokens and train the network.
 */
public class Network {

    // Location of the neural network folder
    private String networkFilePath;

    // List of layers in the network
    private ArrayList<Layer> layers = new ArrayList<>();

    // Attempt to initialize or build the network
    public Network(String networkFilePath) {
        this.networkFilePath = networkFilePath;

        // Attempt to read the network configuration file at networkFilePath address
        try (BufferedReader br = new BufferedReader(new FileReader(networkFilePath + "/config.txt"))) {

            // INITIALIZE LAYERS FROM CONFIG FILE
            // Read the data from the file
            String line = br.readLine();
            boolean readData = false;
            while (line != null) {

                // Check for readData true/false line
                if (line.contains("BEGIN")) {
                    readData = true;
                    line = br.readLine(); // Read the next line to get the first layer configuration
                } else if (line.contains("END")) {
                    readData = false;
                }

                // Use layer configuration data to initialize layers
                if (readData) {
                    String[] configData = line.split("-");
                    if (configData.length < 3) {
                        throw new NumberFormatException("Invalid layer configuration line: " + line);
                    }

                    // Parse layer parameters
                    String layerFile = networkFilePath + "/" + configData[0].trim();
                    int parameterSize = Integer.parseInt(configData[1].trim());
                    int layerSize = Integer.parseInt(configData[2].trim());

                    // Initialize layer
                    layers.add(new Layer(parameterSize, layerSize, layerFile));
                }

                line = br.readLine();
            }
        } catch (IOException e) {
            // CONFIG FILE NOT FOUND, BUILD NEW NETWORK
            try {
                File file = new File(networkFilePath + "/config.txt");
                if (!file.exists()) {
                    file.createNewFile();

                    try (FileWriter fw = new FileWriter(file)) {

                        // Write header data to the config file
                        fw.write("NETWORK CONFIGURATION FILE: " + networkFilePath + "\n\n");
                        fw.write("---------------------------------------\n");
                        fw.write("Layer #.csv - parameterSize - layerSize\n");
                        fw.write("---------------------------------------\n\n");
                        fw.write("BEGIN\n");

                        // Request user input for layer configuration
                        Scanner scan = new Scanner(System.in);
                        System.out.print("New network config detected. Please enter layer count: ");
                        int layerCount = Integer.parseInt(scan.nextLine().trim());

                        // Check layer count validity
                        if (layerCount <= 0) {
                            scan.close();
                            throw new IllegalArgumentException("Layer count must be greater than 0.");
                        }

                        // Request token count for layer zero
                        System.out.print("Please enter number of tokens for network: ");
                        int tokenCount = Integer.parseInt(scan.nextLine().trim());

                        // Check token count validity
                        if (tokenCount <= 0) {
                            scan.close();
                            throw new IllegalArgumentException("Layer count must be greater than 0.");
                        }

                        // Request layerSize (neuron count) for each layer
                        for (int i = 0; i < layerCount; i++) {
                            System.out.print("Layer " + (i) + " layerSize (neuron count): ");
                            int layerSize = Integer.parseInt(scan.nextLine().trim());

                            // Write layer configuration data to file
                            if (i == 0) {
                                // For the first layer, use tokenCount as parameterSize
                                fw.write(i + ".csv - " + tokenCount + " - " + layerSize + "\n");
                                layers.add(new Layer(tokenCount, layerSize, networkFilePath + "/" + i + ".csv"));
                            } else {
                                // For subsequent layers, use previous layerSize as parameterSize
                                int previousLayerSize = layers.get(i - 1).getLayerSize();
                                fw.write(i + ".csv - " + previousLayerSize + " - " + layerSize + "\n");
                                layers.add(new Layer(previousLayerSize, layerSize, networkFilePath + "/" + i + ".csv"));
                            }
                        }
                        fw.write("END");
                        scan.close();
                        System.out.println("Configuration complete.");
                    }

                }
            } catch (IOException ex) {
                // Failure to create config file
                System.err.println("Error creating config file: " + ex.getMessage());
                System.exit(1); // Failure to create new config file
            }
        } catch (NumberFormatException e) {
            // End program with error message if the file is not formatted as expected
            System.err.println("Error reading config file: " + e.getMessage());
            System.exit(1); // Config file format error
        }

        // Print network data
        System.out.println("-------------------\nNETWORK INITIALIZED\n-------------------");
        System.out.println("Network successfully initialized with " + layers.size() + " layers.");
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            System.out.println("Layer " + i + " - layerSize: " + layer.getLayerSize() + ", parameterSize: " + layer.getParameterSize());
        }
    }

    // Process token data through the network
    public double[] processToken(Token token) {
        if (token.getTokenData().length != layers.get(0).getParameterSize()) {
            throw new IllegalArgumentException("Token data size must match the first layer's parameter size.");
        }

        // Save data to temporary array
        double[] inputData = token.getTokenData();

        // Process through each layer
        for (Layer layer : layers) {
            inputData = layer.calculateLayerActivation(inputData);
        }

        // Return final output from the last layer
        return inputData;
    }

    // Pass cost matrix into the final layer and backpropagate through network
    public void trainNetwork(double[] costMatrix) {
        // Check that the cost matrix size matches the last layer's size
        if (costMatrix.length != layers.get(layers.size() - 1).getLayerSize()) {
            throw new IllegalArgumentException("Cost matrix size must match the last layer's size.");
        }

        // Initialize derivative matrix for backpropagation and pass in cost matrix
        double[] derivatveMatrix = costMatrix;

        // Start from the last layer and backpropagate
        for (int i = layers.size(); i > 0; i--) {
            Layer layer = layers.get(i - 1);

            // Extract the derivative matrix from the layer and pass in delta data
            derivatveMatrix = layer.trainLayer(derivatveMatrix);
        }
    }

    // Network save function
    public void saveNetwork() {
        // Save each layer's data to its respective file
        for (Layer layer : layers) {
            layer.saveLayer();
        }
        System.out.println("Network saved successfully.");
    }
}
