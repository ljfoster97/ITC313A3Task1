import java.sql.*;

/**
 * Author: Lyndon Foster.
 * Course: ITC313 - Programming in Java 2.
 * Assessment Title: Assessment Item 3, Task 1 - Tax Management Database Application
 * Date: October 16th, 2021.
 *
 * Object for performing database functions,
 * including establishing a connection, creating a schema and table.
 */

public class DBHandler {
    // Fields.
    private final String url;
    private final String username;
    private final String password;
    private String databaseName;
    private String tableName;
    private Connection connection;

    /**
     * Constructor for DBHandler object.
     * @param url String representing the URL of the SQL server.
     * @param username Username of the account to login to the SQL server.
     * @param password Password of the specified account.
     */
    public DBHandler(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Calls getConnection method from DriverManager class
     * and parses in this objects fields as parameters.
     */
    public void establishConnection() {
        try {
            connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provides a usable connection for performing higher level database functions.
     * @return instance of Connection object.
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Closes the connection to the server.
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new schema and table using the specified names.
     * @param databaseName The name of the schema to create.
     *                     Stored as field for this instance of DBHandler.
     * @param tableName The name of the table to create within that schema.
     *                  Stored as field for this instance of DBHandler.
     * @throws Exception Raises SQLException to the class that called the method for handling.
     */
    public void createDatabase(String databaseName, String tableName) throws Exception {

        this.databaseName = databaseName;
        this.tableName = tableName;

        String sqlCreateDatabase = "CREATE DATABASE IF NOT EXISTS "
                + databaseName;

        String sqlSelectDatabase = "USE " + databaseName;

        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS "
                + tableName
                + " (ID VARCHAR(255) NOT NULL,"
                + " financial_year VARCHAR(255),"
                + " taxable_income DOUBLE(15,2),"
                + " tax DOUBLE(15,2),"
                + " PRIMARY KEY (ID)"
                + ")";
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate(sqlCreateDatabase);
            System.out.println("Created database: " + databaseName);

            statement.executeUpdate(sqlSelectDatabase);
            System.out.println("Selected database: " + databaseName);

            statement.executeUpdate(sqlCreateTable);
            System.out.println("Created table: " + tableName);

        } catch(SQLException e) {
            e.printStackTrace();
            // Raise any exception the class that called the method.
            throw new Exception();
        }
    }

    /**
     * Inserts a new financial record into the schema and table
     * that is associated with this instance of the DBHandler object.
     * @param id ID of the record.
     * @param finYear Financial year of the record.
     * @param taxableIncome The gross income amount.
     * @param tax The amount of tax payable calculated from the gross amount.
     */
    public void update(String id, String finYear, String taxableIncome, String tax){
        try {
            // Create a new PreparedStatement to insert these values into the MySQL DB.
            // '?' are placeholders that can then be set to variables.
            // This could just be a function within the DBHandler class itself.
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO "
                            + databaseName
                            + "."
                            + tableName
                            + " (ID, financial_year, taxable_income, tax) VALUES (?, ?, ?, ?)");
            // Parse variables into the SQL statement.
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, finYear);
            preparedStatement.setString(3, taxableIncome);
            preparedStatement.setString(4, tax);
            // Execute the SQL statement.
            preparedStatement.execute();
            // Catch statement for any SQL errors.
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Looks for an existing record within the database based on the ID provided.
     * @param id ID of the record.
     * @return ResultSet containing the row of data corresponding to the ID.
     */
    public ResultSet search(String id){
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT "
                            + "financial_year, taxable_income, tax"
                            + " FROM "
                            + tableName
                            + " WHERE "
                            + tableName
                            + "."
                            + "ID = ?"
            );
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * Deletes any record in the table that matches the corresponding ID.
     * @param id ID of the record.
     */
    public void delete(String id){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM "
                            + tableName
                            + " WHERE "
                            + tableName
                            + "."
                            + "ID = ?"
            );
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}