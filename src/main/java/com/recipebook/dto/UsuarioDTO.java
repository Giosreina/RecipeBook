package com.recipebook.dto;

public class UsuarioDTO {
    private Integer id_usuario;
    private String nombre_1;
    private String nombre_2;
    private String apellido_1;
    private String apellido_2;
    private String correo_electronico;
    private String username;
    private String password;
    private Integer id_multimedia;
    private String url_multimedia;
    private Integer id_rol;
    private String nombre_rol;

    // Constructor completo
    public UsuarioDTO(Integer id_usuario, String nombre_1, String nombre_2, String apellido_1, 
                      String apellido_2, String correo_electronico, String username, String password, 
                      Integer id_multimedia, String url_multimedia, Integer id_rol, String nombre_rol) {
        this.id_usuario = id_usuario;
        this.nombre_1 = nombre_1;
        this.nombre_2 = nombre_2;
        this.apellido_1 = apellido_1;
        this.apellido_2 = apellido_2;
        this.correo_electronico = correo_electronico;
        this.username = username;
        this.password = password;
        this.id_multimedia = id_multimedia;
        this.url_multimedia = url_multimedia;
        this.id_rol = id_rol;
        this.nombre_rol = nombre_rol;
    }

    // Constructor para registro
    public UsuarioDTO(String nombre_1, String nombre_2, String apellido_1, String apellido_2,
                      String correo_electronico, String username, String password, String url_multimedia) {
        this.nombre_1 = nombre_1;
        this.nombre_2 = nombre_2;
        this.apellido_1 = apellido_1;
        this.apellido_2 = apellido_2;
        this.correo_electronico = correo_electronico;
        this.username = username;
        this.password = password;
        this.url_multimedia = url_multimedia;
        this.id_rol = 1; // rol por defecto
    }

    // Getters y Setters
    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_1() {
        return nombre_1;
    }

    public void setNombre_1(String nombre_1) {
        this.nombre_1 = nombre_1;
    }

    public String getNombre_2() {
        return nombre_2;
    }

    public void setNombre_2(String nombre_2) {
        this.nombre_2 = nombre_2;
    }

    public String getApellido_1() {
        return apellido_1;
    }

    public void setApellido_1(String apellido_1) {
        this.apellido_1 = apellido_1;
    }

    public String getApellido_2() {
        return apellido_2;
    }

    public void setApellido_2(String apellido_2) {
        this.apellido_2 = apellido_2;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Integer getId_rol() {
        return id_rol;
    }

    public void setId_rol(Integer id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }
}
