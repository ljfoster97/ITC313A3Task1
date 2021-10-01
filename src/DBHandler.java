import java.sql.*;

/**
 * Author: Lyndon Foster.
 * Course: ITC313 - Programming in Java 2.
 * Assessment Title: Assessment Item 3, Task 1 - Tax Management Database Application
 * Date: September 26th, 2021.
 */

// Object for encapsulation of database functions.
public class DBHandler {

    // Fields.
    private final String url;
    private final String username;
    private final String password;
    private String databaseName;
    private String tableName;
    private Connection connection;

    // Constructor.
    public DBHandler(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // Calls getConnection method from DriverManager class and parses in this objects fields as parameters.
    public void establishConnection() {
        try {
            connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Provides a usable connection for performing higher level database functions.
    public Connection getConnection() {
        return this.connection;
    }

    // Closes the connection to the database.
    public void closeConnection() {
        try {
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Ideally for this DBHandler Class there would be dedicated functions for creating
    // and updating schemas and tables on the MySQL server.
    // Given the limited scope of this task, it makes more sense to have a
    // single function that can create the initial schema and results table.
    // However this could easily be expanded to allow for multiple tables so
    // that data isn't being overwritten each time the raw data is read in.
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

    public void delete(String id){
        try{
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