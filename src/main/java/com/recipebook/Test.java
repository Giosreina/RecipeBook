package com.recipebook;

import com.recipebook.dao.SQLController;

public class Test {
    public static void main(String[] args) {
        String connectionUrl = "jdbc:sqlserver://sql.bsite.net\\MSSQL2016;databaseName=jkvely_Recipes;user=jkvely_Recipes;password=1029;trustServerCertificate=true";
        SQLController sqlController = new SQLController(connectionUrl);

        if (sqlController.isConnected()) {
            System.out.println("Connected to the database.");
        } else {
            System.out.println("Failed to connect to the database.");
            return;
        }
    }
}

