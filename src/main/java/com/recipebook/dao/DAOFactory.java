package com.recipebook.dao;

public class DAOFactory {
    private static final String URL  = "jdbc:postgresql://localhost:5432/recipebook";
    private static final String USER = "giosreina";
    private static final String PASS = "Kabuto43*";

    /** Devuelve un SQLController con conexión fresca. Ciérralo al terminar. */
    public static SQLController crearConexion() {
        return new SQLController(URL, USER, PASS);
    }
}