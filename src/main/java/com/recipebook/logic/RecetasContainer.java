package com.recipebook.logic;

import java.util.ArrayList;
import java.util.List;

public class RecetasContainer {
    private List<Receta> recetas;

    public RecetasContainer() {
        this.recetas = new ArrayList<>();
    }

    public RecetasContainer(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public void addReceta(Receta receta) {
        recetas.add(receta);
    }

    public void removeReceta(Receta receta) {
        recetas.remove(receta);
    }

    public Receta selectReceta(int index) {
        if (index >= 0 && index < recetas.size()) {
            return recetas.get(index);
        }
        return null;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public int size() {
        return recetas.size();
    }

    public boolean isEmpty() {
        return recetas.isEmpty();
    }

    public List<Receta> getRecetasByTipo(String tipo) {
        List<Receta> resultado = new ArrayList<>();
        for (Receta receta : recetas) {
            if (receta.getTipo().equals(tipo)) {
                resultado.add(receta);
            }
        }
        return resultado;
    }
}
