import data_interface.DataTank;
import data_interface.Viewer;
import engine.Network;
import engine.TrainingToken;

public class App {
    public static void main(String[] args) throws Exception {

        // Initialize DataTank and Viewer
        DataTank dataTank = new DataTank("digit/lib/mnist_train.csv");
        Viewer viewer = new Viewer(28, 28);

    
        // Initialize the neural network and process the token data
        Network network = new Network("digit/src/engine/network_data");
        TrainingToken testToken = dataTank.getTokenSet(1).get(0);

        // Save built image to a file
        java.awt.image.BufferedImage img = viewer.buildImage(testToken.getTokenData());
        javax.imageio.ImageIO.write(img, "png", new java.io.File("output.png"));

        double[] output = network.processTokenData(testToken.getTokenData());
        System.out.println("\n\n" + java.util.Arrays.toString(output));
    }
}