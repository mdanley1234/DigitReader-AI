import data_interface.DataTank;
import data_interface.Viewer;
import engine.Network;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {

        // Initialize DataTank and Viewer
        DataTank dataTank = new DataTank("digit/lib/mnist_train.csv");
        Viewer viewer = new Viewer(28, 28);

        // Request data for a random digit
        ArrayList<Integer> randomDigits = dataTank.getRandomData();

        // Save built image to a file
        java.awt.image.BufferedImage img = viewer.buildImage(randomDigits);
        javax.imageio.ImageIO.write(img, "png", new java.io.File("output.png"));

        // Process CSV data into token data
        double[] tokenData = new double[randomDigits.size()];
        for (int i = 0; i < randomDigits.size(); i++) {
            tokenData[i] = randomDigits.get(i) / 255.0; // Normalize pixel values to [0, 1]
        }

        // Initialize the neural network and process the token data
        Network network = new Network("digit/src/engine/network_data");
        double[] output = network.processTokenData(tokenData);
        System.out.println("\n\n" + java.util.Arrays.toString(output));
    }
}