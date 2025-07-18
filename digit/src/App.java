import data_interface.CSVReader;
import data_interface.DigitToken;
import java.util.ArrayList;
import neural_engine.Network;
import neural_engine.Token;
import neural_engine.Trainer;

public class App {
    public static void main(String[] args) throws Exception {

        // Initialize the neural network | ENTER NETWORK FILE PATH HERE
        Network network = new Network("digit/src/network_data/large_model");

        // Initialize CSV readers, Viewer, and Trainer
        CSVReader trainReader = new CSVReader("digit/lib/mnist_train.csv");
        CSVReader testReader = new CSVReader("digit/lib/mnist_train.csv");
        CSVReader kraggleReader = new CSVReader("digit/lib/kraggle_train.csv");
        // DigitViewer viewer = new DigitViewer(28, 28);
        Trainer trainer = new Trainer(network);

        evaluateNetwork(network, kraggleReader);

        ArrayList<DigitToken> digitTokens = kraggleReader.getTokens(10000);
        ArrayList<Token> tokens = new ArrayList<>(digitTokens);
        // trainer.trainNetwork(tokens);

        evaluateNetwork(network, kraggleReader);
    }

    // Method used to evalaute the network's accuracy
    private static void evaluateNetwork(Network network, CSVReader reader) {

        int numberOfTokens = 1000; // Number of tokens used to evaluate

        // Statistic Counters
        ArrayList<Double> correctConfidenceList = new ArrayList<>();
        ArrayList<Double> incorrectConfidenceList = new ArrayList<>();

        for (DigitToken tokenObject : reader.getTokens(numberOfTokens)) {
            double[] outputData = network.processToken(tokenObject);
            int maxOutput = 0;
            for (int i = 1; i < outputData.length; i++) {
                if (outputData[i] > outputData[maxOutput]) {
                    maxOutput = i;
                }
            }
            if (maxOutput == tokenObject.getDigit()) {
                correctConfidenceList.add(calculateConfidence(outputData, outputData[maxOutput]));
            }
            else {
                incorrectConfidenceList.add(calculateConfidence(outputData, outputData[maxOutput]));
            }
        }
        System.out.println("Network accuracy: " + (correctConfidenceList.size() / (double) numberOfTokens) * 100 + "%");
        
        // Print correct confidence average percentage
        if (!correctConfidenceList.isEmpty()) {
            double sum = 0.0;
            for (double conf : correctConfidenceList) {
                sum += conf;
            }
            double avg = sum / correctConfidenceList.size();
            System.out.println("Average confidence (correct): " + String.format("%.1f", avg * 100) + "%");
        } else {
            System.out.println("No correct answers to calculate average confidence.");
        }

        // Print incorrect confidence average percentage
        if (!incorrectConfidenceList.isEmpty()) {
            double sum = 0.0;
            for (double conf : incorrectConfidenceList) {
                sum += conf;
            }
            double avg = sum / incorrectConfidenceList.size();
            System.out.println("Average confidence (incorrect): " + String.format("%.1f", avg * 100) + "%");
        } else {
            System.out.println("No incorrect answers to calculate average confidence.");
        }

    }

    // Calculate the confidence of the network's ouput
    private static double calculateConfidence(double[] data, double maxOutput) {
        double sum = 0.0;
        for (double value : data) {
            sum += value;
        }
        return (maxOutput / sum); // Return confidence
    }
}