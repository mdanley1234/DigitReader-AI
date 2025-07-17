package data_interface;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * The viewer class is used to convert CSV data in a DigitToken into a
 * BufferedImage object
 */
public class Viewer {

    // Image dimensions in pixels
    private int px_width;
    private int px_height;

    // Initialize viewer with set dimensions
    public Viewer(int px_width, int px_height) {
        this.px_width = px_width;
        this.px_height = px_height;
    }

    // Process a DigitToken and return an image
    public BufferedImage buildImage(DigitToken token) {

        // Pull image data from the token
        int[] imageData = token.getImageData();

        // Check for valid dimensions
        if (imageData.length != px_width * px_height) {
            throw new IllegalArgumentException("CSV data does not match specified dimensions.");
        }

        // Build image
        BufferedImage image = new BufferedImage(px_width, px_height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();

        // Iterator counter
        int counter = 0;

        for (int i = 0; i < px_height; i++) {
            for (int j = 0; j < px_width; j++) {
                // Get pixel value from csvList
                int pixelValue = imageData[counter];

                // Build image here
                raster.setSample(j, i, 0, pixelValue); // Assuming grayscale, set the red channel

                counter++;
            }
        }

        return image;
    }
}
