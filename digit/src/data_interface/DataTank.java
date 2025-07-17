package data_interface;

import engine.TrainingToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DataTank {

    // The digit array stores 10 lists of corresponding CSV data lists for digits 0-9
    ArrayList<ArrayList<Integer>>[] digitArray = new ArrayList[10];

    // Random object
    Random rand = new Random();

    public DataTank(String filePath) {
        // Initialize CSV file for data extraction
        java.io.File file = new java.io.File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("Invalid file path: " + filePath);
        }

        // Build lists for each digit
        for (int i = 0; i < 10; i++) {
            digitArray[i] = new ArrayList<>();
        }

        // Compile lists from the CSV file
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // Example: process values, assuming first value is digit index and rest are integers
                if (values.length > 1) {
                    try {
                        int digitIndex = Integer.parseInt(values[0]);
                        ArrayList<Integer> digitData = new ArrayList<>();
                        for (int i = 1; i < values.length; i++) {
                            digitData.add(Integer.parseInt(values[i]));
                        }
                        digitArray[digitIndex].add(digitData);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get random CSV data from the specified digit's list
    public ArrayList<Integer> getRandomData(int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("Digit must be between 0 and 9.");
        }
        return digitArray[digit].get((int) (rand.nextDouble() * digitArray[digit].size()));
    }

    // Get random CSV data for any digit
    public ArrayList<Integer> getRandomData() {
        return getRandomData( (int) (rand.nextDouble() * 10));
    }

    // Get balanced list of training tokens (non-randomized distribution)
    public ArrayList<TrainingToken> getTokenSet(int numberOfTokens) {

        // TrainingToken list
        ArrayList<TrainingToken> tokens = new ArrayList<>();

        int i = (int) (rand.nextDouble() * 10); // Counter used to increment the digit of each token
        while (tokens.size() < numberOfTokens) {

            // Expectation CSV array conversion
            double[] activationExpectation = new double[10];
            activationExpectation[i] = 1;

            // Digit CSV array conversion (255 compression)
            double[] postData = new double[784]; // TODO: SPECIFICALLY SET FOR 28x28
            ArrayList<Integer> preData = getRandomData(i);
            for (int j = 0; j < preData.size(); j++) {
                postData[j] = preData.get(j) / 255.0;
            }

            // Add token
            tokens.add(new TrainingToken(postData, activationExpectation));

            // Increment digit counter
            if (i == 9) {
                i = 0;
            }
            else {
                i++;
            }
        }

        return tokens;
    }
}