package com.recipebook.dto;

import java.sql.Timestamp;

public class ValorationDTO {
    private Integer id_valoracion;
    private Integer id_receta;
    private Integer id_usuario;
    private String comentario;
    private Timestamp fecha_comentario;
    private Integer valor;

    // Constructor completo
    public ValorationDTO(Integer id_valoracion, Integer id_receta, Integer id_usuario, 
                        String comentario, Timestamp fecha_comentario, Integer valor) {
        this.id_valoracion = id_valoracion;
        this.id_receta = id_receta;
        this.id_usuario = id_usuario;
        this.comentario = comentario;
        this.fecha_comentario = fecha_comentario;
        this.valor = valor;
    }

    // Constructor para creación
    public ValorationDTO(Integer id_receta, Integer id_usuario, String comentario, Integer valor) {
        this.id_receta = id_receta;
        this.id_usuario = id_usuario;
        this.comentario = comentario;
        this.valor = valor;
        this.fecha_comentario = new Timestamp(System.currentTimeMillis());
    }

    // Getters y Setters
    public Integer getId_valoracion() {
        return id_valoracion;
    }

    public void setId_valoracion(Integer id_valoracion) {
        this.id_valoracion = id_valoracion;
    }

    public Integer getId_receta() {
        return id_receta;
    }

    public void setId_receta(Integer id_receta) {
        this.id_receta = id_receta;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Timestamp getFecha_comentario() {
        return fecha_comentario;
    }

    public void setFecha_comentario(Timestamp fecha_comentario) {
        this.fecha_comentario = fecha_comentario;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}
