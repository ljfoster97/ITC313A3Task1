import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void getTaxRates() {
        String currentLine = "";
        try {
            IncomeRange incomeRange = null;
            TaxModel taxModel = null;
            Scanner scanner = new Scanner(new File(TAXRATESPATH));
            String line = "";
            int rowIndex = 1;
            // Skip the header on the first line.
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                StringBuilder cleanLine = new StringBuilder();
                currentLine = scanner.nextLine();
                currentLine = currentLine.replaceAll("[,]", "");
                Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
                Matcher matcher = regex.matcher(currentLine);
                while (matcher.find()){
                    cleanLine.append(matcher.group(1)).append(" ");
                }
                String[] stringArray = cleanLine.toString().split(" ");
                double[] doubleArray = new double[stringArray.length];
                for(int j = 0; j < stringArray.length; j++) {
                    try {
                        doubleArray[j] = Double.parseDouble(stringArray[j]);
                    } catch(NumberFormatException e) {
                        System.out.println("Error! NumberFormatException occurred at index " + j);
                    }
                }
                if(rowIndex == 1){
                    // DO VALIDATION FOR BASETAX AND PERAMOUNT ON FIRST LINE
                    lowerThresholdA = doubleArray[0];
                    thresholdA = doubleArray[1];
                    taxRateA = doubleArray[2]/100d; //all taxrates are in cents, so need to be /100

                    taxModel = new TaxModel(taxRateA,0,thresholdA,1);
                    incomeRange = new IncomeRange(lowerThresholdA,thresholdA);
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
                    baseTaxE = doubleArray[1];
                    taxRateE = doubleArray[2]/100d;
                    perE = doubleArray[3];

                    taxModel = new TaxModel(taxRateE,baseTaxE,thresholdE,perE);
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

