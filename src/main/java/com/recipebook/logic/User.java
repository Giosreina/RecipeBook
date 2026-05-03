package com.recipebook.logic;

import java.util.ArrayList;

public class User {
    private Integer id_usuario = 0;
    private String nombre_1;
    private String nombre_2;
    private String apellido_1;
    private String apellido_2;
    private String urlMultimedia;
    private String correo;
    private String rol;
    private String username;
    private String password;
    private RecetasContainer recetas;

    public User(String nombre_1, String nombre_2, String apellido_1, String apellido_2, String correo, String username, String password, String urlMultimedia){
        this.nombre_1 = nombre_1;
        this.nombre_2 = nombre_2;
        this.apellido_1 = apellido_1;
        this.apellido_2 = apellido_2;
        this.urlMultimedia = urlMultimedia;
        this.correo = correo;
        this.urlMultimedia = urlMultimedia;
        this.username = username;
        this.password = password;
        recetas = new RecetasContainer();        
    }
    public User(Integer id_usuario, String nombre_1, String nombre_2, String apellido_1, String apellido_2, String correo, String username, String rol ,String password, String urlMultimedia) {
        this.id_usuario = id_usuario;
        this.nombre_1 = nombre_1;
        this.nombre_2 = nombre_2;
        this.apellido_1 = apellido_1;
        this.apellido_2 = apellido_2;
        this.urlMultimedia = urlMultimedia;
        this.correo = correo;
        this.rol = rol;
        this.urlMultimedia = urlMultimedia;
        this.username = username;
        this.password = password;
        recetas = new RecetasContainer();
    }

    public void addReceta(Receta receta){
        recetas.addReceta(receta);
    }

    public void deleteReceta(Receta receta){
        recetas.removeReceta(receta);
    }

    public Receta selectReceta(int id){
        return recetas.selectReceta(id - 1);
    }

    public RecetasContainer getRecetas(){
        return recetas;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public void setRecetas(RecetasContainer recetas){
        this.recetas = recetas;
    }

    public boolean validatePassword(String password){
        return this.password.equals(password);
    }
    public void setNombre_1(String nombre_1){
        this.nombre_1 = nombre_1;
    }
    public String getNombre_1(){
        return this.nombre_1;
    }
    public void setNombre_2(String nombre_2){
        this.nombre_2 = nombre_2;
    }
    public String getNombre_2(){
        return this.nombre_2;
    }
    public void setApellido_1(String apellido_1){
        this.apellido_1 = apellido_1;
    }
    public String getApellido_1(){
        return this.apellido_1;
    }
    public void setApellido_2(String apellido_2){
        this.apellido_2 = apellido_2;
    }
    public String getApellido_2(){
        return this.apellido_2;
    }
    public void setUrlMultimedia(String urlMultimedia){
        this.urlMultimedia = urlMultimedia;
    }
    public String getUrlMultimedia(){
        return this.urlMultimedia;
    }
    public void setCorreo(String correo){
        this.correo = correo;
    }
    public String getCorreo(){
        return this.correo;
    }
    public void setRol(String rol){
        this.rol = rol;
    }
    public String getRol(){
        return this.rol;
    }
    public Integer getId_usuario(){
        return this.id_usuario;
    }
}
