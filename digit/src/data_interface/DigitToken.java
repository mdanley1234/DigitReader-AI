package data_interface;

import engine.Token;

/**
 * Special Token subclass designed to directly handle hand-written digit data
 */
public class DigitToken extends Token {

    // Image data of the token
    private int[] imageData;

    // Digit value of the token
    private int digit;

    public DigitToken(int[] tokenData) {
        super(constrain255(tokenData));
        imageData = tokenData;
    }

    public DigitToken(int[] tokenData, int digit) {
        super(constrain255(tokenData), convert10(digit));
        imageData = tokenData;
        this.digit = digit;
    }

    // Scale input data by factor of 255
    private static double[] constrain255(int[] preData) {
        double[] postData = new double[preData.length];
        for (int i = 0; i < preData.length; i++) {
            postData[i] = preData[i] / 255.0;
        }
        return postData;
    }

    // Convert digit value to expectation data array
    private static double[] convert10(int digit) {
        double[] expectationArray = new double[10];
        expectationArray[digit] = 1.0;
        return expectationArray;
    }

    public int[] getImageData() {
        return imageData;
    }

    public int getDigit() {
        return digit;
    }

}
