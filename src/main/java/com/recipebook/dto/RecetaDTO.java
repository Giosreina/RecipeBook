package com.recipebook.dto;

import java.util.ArrayList;
import java.util.List;

public class RecetaDTO {
    private Integer id_receta;
    private String nombre_receta;
    private String descripcion;
    private Integer tiempo_preparacion;
    private Integer id_usuario;
    private String nombre_usuario;
    private Integer id_imagen;
    private String url_imagen;
    private double valoracion;
    private List<String> tipos_receta;
    private List<PasoDTO> pasos;
    private List<String> ingredientes;
    private List<String> utensilios;

    // Constructor completo
    public RecetaDTO(Integer id_receta, String nombre_receta, String descripcion, Integer tiempo_preparacion,
                     Integer id_usuario, String nombre_usuario, Integer id_imagen, String url_imagen, double valoracion) {
        this.id_receta = id_receta;
        this.nombre_receta = nombre_receta;
        this.descripcion = descripcion;
        this.tiempo_preparacion = tiempo_preparacion;
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.id_imagen = id_imagen;
        this.url_imagen = url_imagen;
        this.valoracion = valoracion;
        this.tipos_receta = new ArrayList<>();
        this.pasos = new ArrayList<>();
        this.ingredientes = new ArrayList<>();
        this.utensilios = new ArrayList<>();
    }

    // Constructor para creación
    public RecetaDTO(String nombre_receta, String descripcion, Integer tiempo_preparacion, Integer id_usuario) {
        this.nombre_receta = nombre_receta;
        this.descripcion = descripcion;
        this.tiempo_preparacion = tiempo_preparacion;
        this.id_usuario = id_usuario;
        this.tipos_receta = new ArrayList<>();
        this.pasos = new ArrayList<>();
        this.ingredientes = new ArrayList<>();
        this.utensilios = new ArrayList<>();
        this.valoracion = 0.0;
    }

    // Getters y Setters
    public Integer getId_receta() {
        return id_receta;
    }

    public void setId_receta(Integer id_receta) {
        this.id_receta = id_receta;
    }

    public String getNombre_receta() {
        return nombre_receta;
    }

    public void setNombre_receta(String nombre_receta) {
        this.nombre_receta = nombre_receta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getTiempo_preparacion() {
        return tiempo_preparacion;
    }

    public void setTiempo_preparacion(Integer tiempo_preparacion) {
        this.tiempo_preparacion = tiempo_preparacion;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public Integer getId_imagen() {
        return id_imagen;
    }

    public void setId_imagen(Integer id_imagen) {
        this.id_imagen = id_imagen;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }

    public List<String> getTipos_receta() {
        return tipos_receta;
    }

    public void setTipos_receta(List<String> tipos_receta) {
        this.tipos_receta = tipos_receta;
    }

    public void addTipoReceta(String tipo) {
        this.tipos_receta.add(tipo);
    }

    public List<PasoDTO> getPasos() {
        return pasos;
    }

    public void setPasos(List<PasoDTO> pasos) {
        this.pasos = pasos;
    }

    public void addPaso(PasoDTO paso) {
        this.pasos.add(paso);
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
