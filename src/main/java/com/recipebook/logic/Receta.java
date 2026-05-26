package com.recipebook.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.recipebook.logic.steps.Paso;
import com.recipebook.logic.steps.PasoWextras;

public class Receta {
    private String nombre;
    private String tipo;
    private Optional<String> imagen;
    private Optional<String> descripcion;
    private final List<String> ingredientes;
    private final List<String> utensilios;
    private int tiempo_preparacion; // en minutos.segundos
    private final List<Paso> pasos;
    private double valoracion;
    private String descripcionValoracion;

    public Receta(String nombre, String imagen, String descripcion, String tipo){
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagen = Optional.ofNullable(imagen);
        this.descripcion = Optional.ofNullable(descripcion);
        ingredientes = new ArrayList<>();
        utensilios = new ArrayList<>();
        pasos = new ArrayList<>();
    }
    
    public void addStep(String descripcion, int tiempo, String[] utensilios, String[] ingredientes, String imagen){
        int id = pasos.size() + 1;
        pasos.add(new PasoWextras(id, descripcion, tiempo, utensilios, ingredientes, imagen));
    }

    public void addStep(String descripcion, int tiempo, String imagen){
        int id = pasos.size() + 1;
        pasos.add(new Paso(id, descripcion, tiempo, imagen));
    }
    
    public void deleteStep(int id){
        int _id = id - 1;
        pasos.remove(_id);
    }

    public Paso selectStep(int id){
        int _id = id - 1;
        return pasos.get(_id);
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public String getTipo(){
        return this.tipo;
    }

    public void setImagen(String imagen){
        this.imagen = Optional.ofNullable(imagen);
    }

    public String getImagen(){
        return this.imagen.orElse(null);
    }

    public void setDescripcion(String descripcion){
        this.descripcion = Optional.ofNullable(descripcion);
    }

    public String getDescripcion(){
        return this.descripcion.orElse(null);
    }

    public void addIngrediente(String ingrediente){
        ingredientes.add(ingrediente);
    }

    public void deleteIngrediente(String ingrediente){
        ingredientes.remove(ingrediente);
    }

    public List<String> getIngredientes(){
        return ingredientes;
    }

    public void addUtensilio(String utensilio){
        utensilios.add(utensilio);
    }

    public void deleteUtensilio(String utensilio){
        utensilios.remove(utensilio);
    }

    public List<String> getUtensilios(){
        return utensilios;
    }

    public int getTiempo(){
        int tiempo= 0;
        for(Paso paso : pasos){
            tiempo += paso.getTiempo();
        }
        this.tiempo_preparacion = tiempo;
        return this.tiempo_preparacion;
    }

    public void setTiempo(int tiempo){
        this.tiempo_preparacion = tiempo;
    }

    public List<Paso> getPasos(){
        return pasos;
    }
    public double getValoracion() {
        return valoracion;
    }
    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }
    
    public String getDescripcionValoracion() {
        return descripcionValoracion;
    }
    
    public void setDescripcionValoracion(String descripcionValoracion) {
        this.descripcionValoracion = descripcionValoracion;
    }
}