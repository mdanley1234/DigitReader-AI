package data_interface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DataTank {

    // List of DigitToken objects
    ArrayList<DigitToken> tokens = new ArrayList<>();

    // Random object
    Random rand = new Random();

    public DataTank(String filePath) {
        // Initialize CSV file for data extraction
        java.io.File file = new java.io.File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Invalid file path: " + filePath);
        }

        // Compile lists from the CSV file
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // Assume first value is digit index and rest are integers representing image data
                if (values.length > 1) {
                    try {
                        int[] dataLine = new int[values.length - 1];

                        // Extract preProcessed data from the CSV data line
                        int digit = Integer.parseInt(values[0]);
                        for (int i = 0; i < dataLine.length; i++) {
                            dataLine[i] = Integer.parseInt(values[i + 1]);
                        }

                        // Construct and store data tokens
                        tokens.add(new DigitToken(dataLine, digit));

                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                        System.err.println("Invalid data formatting.");
                        System.exit(1); // Invalid data formatting error
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("File corruption error.");
            System.exit(1); // File corruption error
        }
    }

    public DigitToken getToken() {
        return tokens.get((int) (rand.nextDouble() * tokens.size()));
    }

    public ArrayList<DigitToken> getTokens(int numberOfTokens) {
        ArrayList<DigitToken> basket = new ArrayList<>();
        for (int i = 0; i < numberOfTokens; i++) {
            basket.add(getToken());
        }
        return basket;
    }
}