package engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The network class holds all the layers of the neural network
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

            // Read the data from the file
            String line = br.readLine();
            boolean readData = false;
            while (line != null) {

                // Check for readData true/false setter line
                if (line.contains("BEGIN")) {
                    readData = true;
                    line = br.readLine(); // Read the next line to get the first layer configuration
                }
                else if (line.contains("END")) {
                    readData = false;
                }

                if (readData) {
                    // Extract layer configuration data line
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
            // If the config file does not exist, create a new config file
            try {
                File file = new File(networkFilePath + "/config.txt");
                if (!file.exists()) {
                    file.createNewFile();

                    try (FileWriter fw = new FileWriter(file)) {
                        
                        // Write header data
                        fw.write("NETWORK CONFIGURATION FILE: /layer_values\n\n");
                        fw.write("----------------------------------------\n");
                        fw.write("Layer ##.csv - parameterSize - layerSize\n");
                        fw.write("----------------------------------------\n\n");
                        fw.write("BEGIN\n");

                        // Request user input for layer configuration

                    }



                }
            } catch (IOException ex) {
                // Failure to create config file
                System.err.println("Error creating config file: " + ex.getMessage());
                ex.printStackTrace();
                System.exit(3); // Failure to create new config file
            }
        } catch (NumberFormatException e) {
            // End program with error message if the file is not formatted as expected
            System.err.println("Error reading config file: " + e.getMessage());
            e.printStackTrace();
            System.exit(4); // Config file format error
        }

    }

}
