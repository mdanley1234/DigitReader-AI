import data_interface.DataTank;
import data_interface.DigitToken;
import data_interface.Viewer;
import engine.Network;

public class App {
    public static void main(String[] args) throws Exception {

        // Initialize DataTank and Viewer
        DataTank dataTank = new DataTank("digit/lib/mnist_test.csv");
        Viewer viewer = new Viewer(28, 28);
    
        // Request token data
        DigitToken token = dataTank.getToken();
        System.out.println("Token digit: " + token.getDigit());

        // Initialize the neural network
        Network network = new Network("digit/src/engine/network_data");

        // Save token image data to output.png file
        java.awt.image.BufferedImage img = viewer.buildImage(token);
        javax.imageio.ImageIO.write(img, "png", new java.io.File("output.png"));

        // // Process token
        // double[] output = network.processToken(token);
        // // System.out.println("\n\n" + java.util.Arrays.toString(output));
        // int max = 0;
        // for (int i = 1; i < output.length; i++) {
        //     if (output[i] > output[max]) {
        //         max = i;
        //     }
        // }
        // System.out.println("Network output: " + max + " with confidence " + output[max]);

        // Test network accuracy
        int correct = 0;
        for (DigitToken tokenObject : dataTank.getTokens(5000)) {
            double[] outputData = network.processToken(tokenObject);
            int maxOutput = 0;
            for (int i = 1; i < outputData.length; i++) {
                if (outputData[i] > outputData[maxOutput]) {
                    maxOutput = i;
                }
            }
            if (maxOutput == tokenObject.getDigit()) {
                correct++;
            }
        }
        System.out.println("Network accuracy: " + (correct / 5000.0) * 100 + "%");

        // Trainer trainer = new Trainer(network);
        // trainer.trainNetwork(token);

        // for (Token tokenObject : dataTank.getTokens(50000)) {
        //     trainer.trainNetwork(tokenObject);
        // }
        // network.saveNetwork();

        // double[] output2 = network.processToken(token);
        // System.out.println("\n\n" + java.util.Arrays.toString(output2));
    }
}