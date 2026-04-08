package com.recipebook.logic.steps;

import java.util.Optional;

public class Paso {
    private final int id;
    private String descripcion;
    private int tiempo;
    private Optional<String> imagen;
 
    public Paso(int id, String descripcion, int tiempo, String imagen) {
        this.id = id;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.imagen = Optional.ofNullable(imagen);
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getTiempo() {
        return tiempo;
    }

    public String getImagen() {
        return imagen.orElse(null);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public void setImagen(String imagen) {
        this.imagen = Optional.ofNullable(imagen);
    }
}