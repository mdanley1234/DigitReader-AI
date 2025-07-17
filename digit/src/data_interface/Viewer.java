package data_interface;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;



public class Viewer {

    // Image dimensions in pixels
    private int px_width;
    private int px_height;

    public Viewer(int px_width, int px_height) {
        this.px_width = px_width;
        this.px_height = px_height;
    }

    // Secondary method for use with ArrayList TODO: TEMP INFLATION METHOD
    public BufferedImage buildImage(double[] csvArray) {

        ArrayList<Integer> csvList = new ArrayList<>();

        for (int i = 0; i < csvArray.length; i++) {
            csvList.add((int) (csvArray[i] * 255));
        }

        return buildImage(csvList);
    }

    // Process a csv line and return an image
    public BufferedImage buildImage(ArrayList<Integer> csvList) {
        // Check for valid dimensions
        if (csvList.size() != px_width * px_height) {
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
                int pixelValue = csvList.get(counter);

                // Build image here
                raster.setSample(j, i, 0, pixelValue); // Assuming grayscale, set the red channel

                counter++;
            }
        }

        return image;
    }
}