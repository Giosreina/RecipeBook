package com.recipebook.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * VistaDao — Data Access Object para las vistas SQL del dashboard.
 * 
 * Este DAO consume las siguientes vistas de PostgreSQL:
 *   1. recetas_mejor_valoradas  → Top 5 recetas (Simplificación)
 *   2. reporte_recetas          → Reporte por categoría (Reporte)
 *   3. usuario_receta           → Usernames sin datos sensibles (Seguridad)
 *   4. v_recetas_rapidas        → Recetas <= 30 min (Actualizable)
 * 
 * Los datos son consumidos por dashboard.jsp para su visualización.
 * 
 * @see com.recipebook.servlet.IndexServlet
 */
@SuppressWarnings("CallToPrintStackTrace")
public class VistaDao {

    private final SQLController sqlController;

    public VistaDao(SQLController sqlController) {
        this.sqlController = sqlController;
    }

    public static class RecetaMejorValorada {
        public final String usuario;
        public final String receta;
        public final double valoracionPromedio;

        public RecetaMejorValorada(String usuario, String receta, double valoracionPromedio) {
            this.usuario            = usuario;
            this.receta             = receta;
            this.valoracionPromedio = valoracionPromedio;
        }

        @Override
        public String toString() {
            return String.format("RecetaMejorValorada{usuario='%s', receta='%s', promedio=%.2f}",
                    usuario, receta, valoracionPromedio);
        }
    }

    /** Resultado de reporte_recetas */
    public static class ReporteReceta {
        public final String tipoReceta;
        public final double promedioValoracion;
        public final int    totalRecetas;

        public ReporteReceta(String tipoReceta, double promedioValoracion, int totalRecetas) {
            this.tipoReceta         = tipoReceta;
            this.promedioValoracion = promedioValoracion;
            this.totalRecetas       = totalRecetas;
        }

        @Override
        public String toString() {
            return String.format("ReporteReceta{tipo='%s', promedio=%.2f, total=%d}",
                    tipoReceta, promedioValoracion, totalRecetas);
        }
    }

    /** Resultado de v_recetas_rapidas */
    public static class RecetaRapida {
        public final String receta;
        public final int    minutos;
        public final String descripcion;

        public RecetaRapida(String receta, int minutos, String descripcion) {
            this.receta      = receta;
            this.minutos     = minutos;
            this.descripcion = descripcion;
        }

        @Override
        public String toString() {
            return String.format("RecetaRapida{receta='%s', minutos=%d}", receta, minutos);
        }
    }

    //  VISTA 1 — recetas_mejor_valoradas

    /**
     * Retorna el top 5 de recetas con mayor promedio de valoración.
     * Usa la vista recetas_mejor_valoradas (JOIN de RECETAS + VALORACION + USUARIO).
     *
     * @return Lista de hasta 5 RecetaMejorValorada, ordenadas de mayor a menor promedio.
     */
    public List<RecetaMejorValorada> obtenerRecetasMejorValoradas() {
        List<RecetaMejorValorada> resultado = new ArrayList<>();
        String query = "SELECT usuario, receta, valoracion_promedio FROM recetas_mejor_valoradas";

        try {
            ResultSet rs = sqlController.executeQuery(query);
            while (rs.next()) {
                resultado.add(new RecetaMejorValorada(
                        rs.getString("usuario"),
                        rs.getString("receta"),
                        rs.getDouble("valoracion_promedio")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }


    //  VISTA 2 — reporte_recetas

    /**
     * Retorna el reporte de promedio de valoración y total de recetas
     * agrupado por tipo de receta.
     * Usa la vista reporte_recetas.
     *
     * @return Lista de ReporteReceta ordenada por total de recetas descendente.
     */
    public List<ReporteReceta> obtenerReporteRecetas() {
        List<ReporteReceta> resultado = new ArrayList<>();
        String query = "SELECT tipo_receta, promedio_valoracion, total_recetas FROM reporte_recetas";

        try {
            ResultSet rs = sqlController.executeQuery(query);
            while (rs.next()) {
                resultado.add(new ReporteReceta(
                        rs.getString("tipo_receta"),
                        rs.getDouble("promedio_valoracion"),
                        rs.getInt("total_recetas")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    /**
     * Retorna el reporte filtrado por un tipo de receta específico.
     *
     * @param tipoReceta Nombre del tipo (ej: "Almuerzo", "Postre").
     * @return ReporteReceta del tipo solicitado, o null si no existe.
     */
    public ReporteReceta obtenerReportePorTipo(String tipoReceta) {
        String query = String.format(
                "SELECT tipo_receta, promedio_valoracion, total_recetas " +
                "FROM reporte_recetas WHERE tipo_receta = '%s'",
                escapeSQLString(tipoReceta)
        );

        try {
            ResultSet rs = sqlController.executeQuery(query);
            if (rs.next()) {
                return new ReporteReceta(
                        rs.getString("tipo_receta"),
                        rs.getDouble("promedio_valoracion"),
                        rs.getInt("total_recetas")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //  VISTA 3 — usuario_receta 

    /**
     * Retorna la lista de usernames.
     * Usa la vista de seguridad usuario_receta que oculta
     * contraseña, correo y demás datos sensibles del usuario.
     *
     * @return Lista de usernames (todos los usuarios, incluidos sin recetas).
     */
    public List<String> obtenerUsernamesConRecetas() {
        List<String> usernames = new ArrayList<>();
        String query = "SELECT username FROM usuario_receta";

        try {
            ResultSet rs = sqlController.executeQuery(query);
            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usernames;
    }

    //  VISTA 4 — v_recetas_rapidas

    /**
     * Retorna todas las recetas con tiempo_preparacion <= 30 minutos.
     * Usa la vista v_recetas_rapidas (con WITH CHECK OPTION activo en BD).
     *
     * @return Lista de RecetaRapida.
     */
    public List<RecetaRapida> obtenerRecetasRapidas() {
        List<RecetaRapida> resultado = new ArrayList<>();
        String query = "SELECT receta, minutos, descripcion FROM v_recetas_rapidas";

        try {
            ResultSet rs = sqlController.executeQuery(query);
            while (rs.next()) {
                resultado.add(new RecetaRapida(
                        rs.getString("receta"),
                        rs.getInt("minutos"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    /**
     * Retorna recetas rápidas que contengan cierta palabra en el nombre.
     * Filtro adicional sobre la vista v_recetas_rapidas.
     *
     * @param filtroNombre Texto a buscar en el nombre de la receta.
     * @return Lista de RecetaRapida que coincidan.
     */
    public List<RecetaRapida> obtenerRecetasRapidasPorNombre(String filtroNombre) {
        List<RecetaRapida> resultado = new ArrayList<>();
        String query = String.format(
                "SELECT receta, minutos, descripcion FROM v_recetas_rapidas " +
                "WHERE LOWER(receta) LIKE LOWER('%%%s%%')",
                escapeSQLString(filtroNombre)
        );

        try {
            ResultSet rs = sqlController.executeQuery(query);
            while (rs.next()) {
                resultado.add(new RecetaRapida(
                        rs.getString("receta"),
                        rs.getInt("minutos"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    private String escapeSQLString(String str) {
        if (str == null) return "";
        return str.replace("'", "''");
    }
}