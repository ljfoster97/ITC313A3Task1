import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Author: Lyndon Foster.
 * Course: ITC313 - Programming in Java 2.
 * Assessment Title: Assessment Item 3, Task 1 - Tax Management Database Application
 * Date: October 16th, 2021.
 */
public class Task1 extends Application {
    // Fields for MySQL database.
    static final String BLANK = "";
    static final String DB_NAME = "TaxManagementSystem";
    static final String TABLE_NAME = "TaxResult";
    static final String DB_URL = "jdbc:mysql://localhost:3306?";
    static final String DB_USERNAME = "root";
    static final String DB_PASSWORD = "Zi26303y";


    @Override
    public void start(Stage stage) {
        // Create a dbHandler object for performing DB operations.
        DBHandler dbHandler = setupDatabaseConnection(DB_NAME, TABLE_NAME,
                DB_URL, DB_USERNAME, DB_PASSWORD);

        // Create a new taxController object and read in the tax rates from taxrates.txt file.
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
        incomeField.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(incomeField, 1, 2);

        Label taxLabel = new Label("Tax:");
        gridPane.add(taxLabel, 0, 3);

        TextField taxField = new TextField();
        taxField.setEditable(false);
        taxField.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(taxField, 1, 3);

        // maybe add a separate HBox for these buttons to account for width of Textfields above
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
                this.alert("Input Missing",
                        "Please enter a valid taxable income amount.", AlertType.ERROR);
            } else {
                // TRY CATCH FOR INPUT VALIDATION
                double calculatedTax = 0;
                double taxableIncomeDbl = Double.parseDouble(taxableIncome);
                for (IncomeRange range: TaxController.dataMap.keySet()) {
                    if (taxableIncomeDbl >= range.getLowerLimit() && taxableIncomeDbl <= range.getUpperLimit()) {
                        TaxModel taxModel = TaxController.dataMap.get(range);
                        calculatedTax = taxModel.getBaseTax()
                                + (taxableIncomeDbl - taxModel.getOverLimit()) * (taxModel.getCentsPerDollar());
                    }
                }
                taxField.setText(String.valueOf(calculatedTax));
            }
        });

        buttonUpdate.setOnAction(actionEvent -> {
            String id = idField.getText();
            String finYear = financialYearField.getText();
            String taxableIncome = incomeField.getText();
            String tax = taxField.getText();
            if (BLANK.equals(id)){
                this.alert("Input Missing",
                        "Please enter a valid ID.", AlertType.ERROR);
            }
            else if (BLANK.equals(finYear)){
                this.alert("Input Missing",
                        "Please enter a valid financial year.", AlertType.ERROR);
            }
            else if (BLANK.equals(taxableIncome)){
                this.alert("Input Missing",
                        "Please enter a valid taxable income amount.", AlertType.ERROR);
            }
            else if (BLANK.equals(tax)){
                this.alert("Calculation Error",
                        "Tax must be calculated prior to updating the record.", AlertType.ERROR);
            }
            else {
                try {
                    dbHandler.update(id, finYear, taxableIncome, tax);
                }
                catch(SQLIntegrityConstraintViolationException e) {
                    this.alert("SQL Error", "The data matches an existing record.", AlertType.ERROR);
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonSearch.setOnAction(actionEvent -> {
            String id = idField.getText();
            if (BLANK.equals(id)){
                this.alert("Input Missing",
                        "Please enter a valid ID.", AlertType.ERROR);
            }
            else {
                try {
                    ResultSet resultSet = dbHandler.search(id);
                    if (resultSet.next()){
                        System.out.println("Retrieving Data...");
                        financialYearField.setText(String.valueOf(resultSet.getInt(1)));
                        incomeField.setText(String.valueOf(resultSet.getDouble(2)));
                        taxField.setText("$"+ resultSet.getDouble(3));
                    }
                    else if (!resultSet.next()){
                        alert("Record not found.","No record could be found with the corresponding ID.",
                                AlertType.ERROR);
                    }
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonDelete.setOnAction(actionEvent -> {
            String id = idField.getText();
            if (BLANK.equals(id)){
                this.alert("Input Missing",
                        "Please enter a valid ID.", AlertType.ERROR);
            }
            else {
                Alert alertConfirmation = new Alert(AlertType.CONFIRMATION);
                alertConfirmation.setTitle("Confirmation Required.");
                alertConfirmation.setContentText("This will permanently remove the record from the database." +
                        "\nAre you sure you wish to continue?");
                Optional<ButtonType> result = alertConfirmation.showAndWait();
                 if (result.get() == ButtonType.OK){
                    // Delete the record if user confirms.
                    dbHandler.delete(id);
                    idField.clear();
                    financialYearField.clear();
                    incomeField.clear();
                    taxField.clear();
                // Do nothing if result == cancel or no result, not necessary to check them.
                }
            }
        });

        Scene scene = new Scene(gridPane, 500, 275);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates an alert dialog.
     * @param title Title of the dialog window.
     * @param message Message to display within the dialog window.
     * @param alertType Alert type for the dialog window.
     */
    public void alert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Creates a new DBHandler object with the specified credentials,
     * calls the DBHandler.establishConnection() method to connection to the specified SQL server,
     * Returns a connection object,
     * Creates a new schema and table with the specified names,
     * returns an instance of the DBHandler object to perform further SQL operations.
     * @param databaseName Name of the schema.
     * @param tableName Name of the table.
     * @param url URL of the SQL server.
     * @param username Username to connect to the server with.
     * @param password Password for the specified user.
     * @return Instance of DBHandler.java
     */
    public DBHandler setupDatabaseConnection(String databaseName, String tableName, String url, String username, String password) {
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
            e.printStackTrace();
            this.alert("Fatal Error",
                    "An unresolvable error occured while trying to establish" +
                            " a connection to the SQL Database." +
                            "Please verify that the SQL server is configured correctly.",
                    AlertType.ERROR);
        }
        // Return the Connection object.
        return dbHandler;
    }

}

