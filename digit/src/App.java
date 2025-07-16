import data_interface.DataTank;
import data_interface.Viewer;
import engine.Layer;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        DataTank dataTank = new DataTank("digit/lib/mnist_train.csv");
        Viewer viewer = new Viewer(28, 28);

        // Save built image to a file
        java.awt.image.BufferedImage img = viewer.buildImage(dataTank.getRandomData()); // Assuming Viewer has getImage()
        javax.imageio.ImageIO.write(img, "png", new java.io.File("output.png"));

        Layer layer = new Layer(10, 10, "digit/src/engine/layer_values/0.csv");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Wait for user input to keep the program running
        layer.saveLayer();
    }
}
