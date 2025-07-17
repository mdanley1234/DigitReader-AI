import data_interface.DataTank;
import data_interface.DigitToken;
import data_interface.Viewer;
import engine.Network;

public class App {
    public static void main(String[] args) throws Exception {

        // Initialize DataTank and Viewer
        DataTank dataTank = new DataTank("digit/lib/mnist_train.csv");
        Viewer viewer = new Viewer(28, 28);
    
        // Request token data
        DigitToken token = dataTank.getToken();
        System.out.println("Token digit: " + token.getDigit());

        // Initialize the neural network
        Network network = new Network("digit/src/engine/network_data");

        // Save token image data to output.png file
        java.awt.image.BufferedImage img = viewer.buildImage(token);
        javax.imageio.ImageIO.write(img, "png", new java.io.File("output.png"));

        double[] output = network.processToken(token);
        System.out.println("\n\n" + java.util.Arrays.toString(output));
    }
}