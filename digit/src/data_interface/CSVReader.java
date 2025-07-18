package data_interface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * CSVReader class is used to read CSV files containing digit data
 */
public class CSVReader {

    // List of DigitToken objects
    private final ArrayList<DigitToken> tokens = new ArrayList<>();

    // Random object used to select random tokens
    private final Random rand = new Random();

    /**
     * Constructor that initializes the CSVReader with a file path
     *
     * @param filePath Path to the CSV file containing digit data
     */
    public CSVReader(String filePath) {
        // Initialize CSV file for data extraction
        java.io.File file = new java.io.File(filePath);

        // Check if the file exists and is a valid file
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Invalid file path: " + filePath);
        }

        // Compile tokens from CSV file data
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                // Assume first value is digit index and rest are integers representing image data
                if (values.length > 1) {
                    try {
                        // Prepare dataLine array to hold image data
                        int[] dataLine = new int[values.length - 1];

                        // Extract data from the CSV data line and store it in dataLine
                        int digit = Integer.parseInt(values[0]);
                        for (int i = 0; i < dataLine.length; i++) {
                            dataLine[i] = Integer.parseInt(values[i + 1]);
                        }

                        // Construct and store data token
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

    // Returns a single random DigitToken from the list
    public DigitToken getToken() {
        return tokens.get((int) (rand.nextDouble() * tokens.size()));
    }

    // Returns a list of DigitToken objects with the specified number of tokens
    public ArrayList<DigitToken> getTokens(int numberOfTokens) {
        ArrayList<DigitToken> basket = new ArrayList<>();
        for (int i = 0; i < numberOfTokens; i++) {
            basket.add(getToken());
        }
        return basket;
    }
}