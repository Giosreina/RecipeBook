package com.recipebook.dto;

import java.util.ArrayList;
import java.util.List;

public class PasoDTO {
    private Integer id_paso;
    private Integer id_receta;
    private String descripcion;
    private Integer id_multimedia;
    private String url_multimedia;
    private List<String> ingredientes;
    private List<String> utensilios;

    // Constructor completo
    public PasoDTO(Integer id_paso, Integer id_receta, String descripcion, Integer id_multimedia, String url_multimedia) {
        this.id_paso = id_paso;
        this.id_receta = id_receta;
        this.descripcion = descripcion;
        this.id_multimedia = id_multimedia;
        this.url_multimedia = url_multimedia;
        this.ingredientes = new ArrayList<>();
        this.utensilios = new ArrayList<>();
    }

    // Constructor para creación
    public PasoDTO(Integer id_receta, String descripcion, Integer id_multimedia, String url_multimedia) {
        this.id_receta = id_receta;
        this.descripcion = descripcion;
        this.id_multimedia = id_multimedia;
        this.url_multimedia = url_multimedia;
        this.ingredientes = new ArrayList<>();
        this.utensilios = new ArrayList<>();
    }

    // Getters y Setters
    public Integer getId_paso() {
        return id_paso;
    }

    public void setId_paso(Integer id_paso) {
        this.id_paso = id_paso;
    }

    public Integer getId_receta() {
        return id_receta;
    }

    public void setId_receta(Integer id_receta) {
        this.id_receta = id_receta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getId_multimedia() {
        return id_multimedia;
    }

    public void setId_multimedia(Integer id_multimedia) {
        this.id_multimedia = id_multimedia;
    }

    public String getUrl_multimedia() {
        return url_multimedia;
    }

    public void setUrl_multimedia(String url_multimedia) {
        this.url_multimedia = url_multimedia;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void addIngrediente(String ingrediente) {
        this.ingredientes.add(ingrediente);
    }

    public List<String> getUtensilios() {
        return utensilios;
    }

    public void setUtensilios(List<String> utensilios) {
        this.utensilios = utensilios;
    }

    public void addUtensilio(String utensilio) {
        this.utensilios.add(utensilio);
    }
}
