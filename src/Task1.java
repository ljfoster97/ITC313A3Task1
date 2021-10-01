import java.io.File;
import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Task1 extends Application {
    // Fields for MySQL database.
    static final String DB_NAME = "TaxManagementSystem";
    static final String TABLE_NAME = "TaxResult";
    static final String DB_URL = "jdbc:mysql://localhost:3306?";
    static final String DB_USERNAME = "root";
    static final String DB_PASSWORD = "Zi26303y";


    @Override
    public void start(Stage stage) {
        DBHandler dbHandler = setupDatabaseConnection(DB_NAME, TABLE_NAME,
                DB_URL, DB_USERNAME, DB_PASSWORD);

        TaxController taxController = new TaxController();
        taxController.getTaxRates();

        stage.setTitle("Tax Management Application");
 
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label idLabel = new Label("ID:");
        gridPane.add(idLabel,0,0);

        TextField idField = new TextField();

        gridPane.add(idField,1,0);

        Label financialYearLabel = new Label("Financial Year:");
        gridPane.add(financialYearLabel, 0, 1);

        TextField financialYearField = new TextField();

        gridPane.add(financialYearField,1,1);

        Label incomeLabel = new Label("Taxable Income:");
        gridPane.add(incomeLabel, 0, 2);

        TextField incomeField = new TextField();

        gridPane.add(incomeField, 1, 2);

        Label taxLabel = new Label("Tax:");
        gridPane.add(taxLabel, 0, 3);

        TextField taxField = new TextField();

        taxField.setEditable(false);
        gridPane.add(taxField, 1, 3);

        // maybe add a seperate hbox for these buttons to account for width of textfields
        Button buttonCalculate = new Button("Calculate");
        gridPane.add(buttonCalculate, 2, 5);

        Button buttonSearch = new Button("Search");
        gridPane.add(buttonSearch,0,5);

        Button buttonUpdate = new Button("Update");
        gridPane.add(buttonUpdate, 1, 5);

        Button buttonDelete = new Button("Delete");
        gridPane.add(buttonDelete,3,5 );

        buttonCalculate.setOnAction(actionEvent -> {
            String taxableIncome = incomeField.getText();
            if (TaxController.BLANK.equals(taxableIncome)) {
                this.alert("Input Missing", "Please enter a valid taxable income amount.", AlertType.ERROR);
            } else {
                double calculatedTax = 0;
                double taxableIncomeDbl = Double.parseDouble(taxableIncome);
                for (IncomeRange range: TaxController.dataMap.keySet()) {
                    if (taxableIncomeDbl >= range.getLowerLimit() && taxableIncomeDbl <= range.getUpperLimit()) {
                        TaxModel taxModel = TaxController.dataMap.get(range);
                        System.out.println("taxmodel:"+ taxModel);
                        calculatedTax = taxModel.getBaseTax() + (taxableIncomeDbl - taxModel.getOverLimit()) * (taxModel.getCentsPerDollar());
                    }
                }
                taxField.setText("$"+ calculatedTax);
            }

        });

        Scene scene = new Scene(gridPane, 500, 275);
        stage.setScene(scene);

        stage.show();
    }

    public void alert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static DBHandler setupDatabaseConnection(String databaseName, String tableName, String url, String username, String password) {
        // This database setup can likely be its own function.
        // Create new DBHandler Object and specify the required parameters.
        DBHandler dbHandler = new DBHandler(url, username, password);
        // Call function to establish the connection.
        dbHandler.establishConnection();
        // Create local connection object from the DBHandler.
        Connection connection = dbHandler.getConnection();
        // Call function to create the database and table using the fields specified in this class.
        try {
            dbHandler.createDatabase(databaseName, tableName);
        } catch(Exception e) {
            // Ideally this would throw the exception to the class that called it,
            // instead of creating an error dialog in this catch statement.
            showDialogWindow(Alert.AlertType.ERROR,
                    "A fatal error occured while trying to create the required " +
                            "schema and tables within the database.",
                    "I have no idea what you did to break everything this badly.",
                    "Fatal Error.");
            e.printStackTrace();
        }
        // Return the Connection object.
        return dbHandler;
    }

    // Function to simplify making alerts.
    public static void showDialogWindow(Alert.AlertType alertType, String contentText, String headerText, String titleText) {
        Alert alert = new Alert(alertType);
        alert.setContentText(contentText);
        alert.setHeaderText(headerText);
        alert.setTitle(titleText);
        alert.show();
    }
}

