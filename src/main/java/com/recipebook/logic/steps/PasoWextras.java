package com.recipebook.logic.steps;

public class PasoWextras extends Paso{
    private String[] utensilios;
    private String[] ingredientes;

    public PasoWextras(int id, String descripcion, int tiempo, String[] utensilios, String[] ingredientes, String imagen) {
        super(id, descripcion, tiempo, imagen);
        this.utensilios = utensilios;
        this.ingredientes = ingredientes;
    }

    public String[] getUtensilios() {
        return utensilios;
    }

    public void setUtensilios(String[] utensilios) {
        this.utensilios = utensilios;
    }

    public String[] getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String[] ingredientes) {
        this.ingredientes = ingredientes;
    }
}
