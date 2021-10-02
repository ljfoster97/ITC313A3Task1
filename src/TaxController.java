import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Lyndon Foster.
 * Course: ITC313 - Programming in Java 2.
 * Assessment Title: Assessment Item 3, Task 1 - Tax Management Database Application
 * Date: October 16th, 2021.
 *
 * Object for reading in taxrates.txt and creating a
 * LinkedHashMap data structure for the relation between tax rates and income ranges.
 */
public class TaxController {
    private double lowerThresholdA;
    private double lowerThresholdB;
    private double lowerThresholdC;
    private double lowerThresholdD;
    private double lowerThresholdE;
    private double thresholdA;
    private double thresholdB;
    private double thresholdC;
    private double thresholdD;
    private double thresholdE;
    private double taxRateA;
    private double taxRateB;
    private double taxRateC;
    private double taxRateD;
    private double taxRateE;
    private double baseTaxC;
    private double baseTaxD;
    private double baseTaxE;
    private double perB;
    private double perC;
    private double perD;
    private double perE;

    public static final String BLANK = "";
    public static final String TAXRATESPATH = "taxrates.txt";
    static Map<IncomeRange, TaxModel> dataMap = new LinkedHashMap<>();

    public TaxController() {
    }

    /**
     * Reads in taxrates.txt file, creates an array of doubles for the numerical values contained into each line,
     * creates TaxModel and IncomeRange objects for each of those values,
     * then adds those objects to the LinkedHashMap.
     */
    public void getTaxRates() {
        String currentLine;
        try {
            IncomeRange incomeRange = null;
            TaxModel taxModel = null;
            Scanner scanner = new Scanner(new File(TAXRATESPATH));
            String line = "";
            int rowIndex = 1;
            // Skip the header on the first line of taxrates.txt
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                // New StringBuilder.
                StringBuilder cleanLine = new StringBuilder();
                // Get the current line.
                currentLine = scanner.nextLine();
                // Strip all commas.
                currentLine = currentLine.replaceAll("[,]", "");
                // Regex pattern to remove everything except for numerical characters.
                Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
                Matcher matcher = regex.matcher(currentLine);
                while (matcher.find()){
                    // add the numerical characters to cleanLine.
                    cleanLine.append(matcher.group(1)).append(" ");
                }
                // Add the clean line to an array of strings, using space as delimiter.
                String[] stringArray = cleanLine.toString().split(" ");
                // Create a new array to store the doubles.
                double[] doubleArray = new double[stringArray.length];
                // Iterate through the stringArray and add the double value of the numerical character
                // to the doubleArray.
                for(int j = 0; j < stringArray.length; j++) {
                    try {
                        doubleArray[j] = Double.parseDouble(stringArray[j]);
                    } catch(NumberFormatException e) {
                        System.out.println("Error! NumberFormatException occurred at index " + j);
                    }
                }
                // The number of values in each row differs, so we need to account for that.
                if(rowIndex == 1){
                    // DO VALIDATION FOR BASETAX AND PERAMOUNT ON FIRST LINE
                    lowerThresholdA = doubleArray[0];
                    thresholdA = doubleArray[1];
                    // All taxrates are in cents, so the double value needs to be divided by 100.
                    taxRateA = doubleArray[2]/100d;

                    // Create a new TaxModel object with the tax rate,
                    // base tax amount, threshold amount and per amount.
                    taxModel = new TaxModel(taxRateA,0,thresholdA,1);
                    // Create a new IncomeRange object with the lower and upper income ranges.
                    incomeRange = new IncomeRange(lowerThresholdA,thresholdA);

                    // Add both objects to the LinkedHashMap.
                    dataMap.put(incomeRange,taxModel);

                } else if(rowIndex == 2){
                    // DO VALIDATION FOR BASETAX ON SECOND LINE
                    lowerThresholdB = doubleArray[0];
                    thresholdB = doubleArray[1];
                    taxRateB = doubleArray[2]/100d;
                    perB = doubleArray[3];

                    taxModel = new TaxModel(taxRateB,0,thresholdB, perB);
                    incomeRange = new IncomeRange(lowerThresholdB,thresholdB);

                    dataMap.put(incomeRange,taxModel);
                    // REMOVE REDUNDANT ELIFS
                } else if(rowIndex == 3){
                    lowerThresholdC = doubleArray[0];
                    thresholdC = doubleArray[1];
                    baseTaxC = doubleArray[2];
                    taxRateC = doubleArray[3]/100d;
                    perC = doubleArray[4];

                    taxModel = new TaxModel(taxRateC,baseTaxC,thresholdC, perC);
                    incomeRange = new IncomeRange(lowerThresholdC,thresholdC);

                    dataMap.put(incomeRange,taxModel);

                } else if(rowIndex == 4){
                    lowerThresholdD = doubleArray[0];
                    thresholdD = doubleArray[1];
                    baseTaxD = doubleArray[2];
                    taxRateD = doubleArray[3]/100d;
                    perD = doubleArray[4];

                    taxModel = new TaxModel(taxRateD,baseTaxD,lowerThresholdD,perD);
                    incomeRange = new IncomeRange(lowerThresholdD,thresholdD);

                    dataMap.put(incomeRange,taxModel);
                } else if(rowIndex == 5){
                    lowerThresholdE = doubleArray[0];
                    // THRESHOLD INFINITE?
                    thresholdE = 1000000;
                    baseTaxE = doubleArray[1];
                    taxRateE = doubleArray[2]/100d;
                    perE = doubleArray[3];

                    taxModel = new TaxModel(taxRateE,baseTaxE,lowerThresholdE,perE);
                    incomeRange = new IncomeRange(lowerThresholdE,thresholdE);

                    dataMap.put(incomeRange,taxModel);
                } else{
                    System.out.println("Error! Invalid or corrupted taxrates.txt file.");
                    break;
                }
                rowIndex++;
            }
            System.out.println();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

