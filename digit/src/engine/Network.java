package engine;

import java.io.File;
import java.util.ArrayList;

// The network class holds the layers of the neural network
public class Network {
    
    // Location of the neural network folder
    private String networkFilePath;

    public Network(ArrayList<Integer> layerSizes, String networkFilePath) {
        this.networkFilePath = networkFilePath;

        // Check if the network file path is valid
        File file = new File(null)


        // Initialize layers based on the provided sizes
        for (int i = 0; i < layerSizes.size(); i++) {
            int parameterSize = (i == 0) ? layerSizes.get(i) : layerSizes.get(i - 1);
            Layer layer = new Layer(parameterSize, layerSizes.get(i), networkFilePath + "/layer_values/" + i + ".csv");
            // Add the layer to the network (not shown here)
        }

    }

}
