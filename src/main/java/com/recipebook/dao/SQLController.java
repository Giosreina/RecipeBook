package com.recipebook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("CallToPrintStackTrace")
public class SQLController {
    private Connection connection;
    private final boolean connected;
    private final String connectionUrl = "jdbc:postgresql://localhost:5432/recipebook";

    public SQLController() {
        connected = start(this.connectionUrl, "giosreina", "Kabuto43*");
    }

    public SQLController(String connectionUrl) {
        connected = start(connectionUrl, "giosreina", "Kabuto43*");
    }

    public SQLController(String connectionUrl, String user, String password) {
        connected = start(connectionUrl, user, password);
    }
    
    private boolean start(String connectionUrl, String user, String password) {
        try {
            // Cargar el driver JDBC para PostgreSQL
            Class.forName("org.postgresql.Driver");
            System.out.print("Connecting to PostgreSQL ... ");
            connection = DriverManager.getConnection(connectionUrl, user, password);
            System.out.println("Done.");
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println();
            e.printStackTrace();
            return false;
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        if (isConnected()) {
            Statement statement = connection.createStatement();
            statement.closeOnCompletion();
            return statement.executeQuery(query);
        } else {
            throw new SQLException("Not connected to the database.");
        }
    }

    public int executeUpdate(String query) throws SQLException {
        if (isConnected()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } else {
            throw new SQLException("Not connected to the database.");
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean getConnected() {
        return connected;
    }
    
}