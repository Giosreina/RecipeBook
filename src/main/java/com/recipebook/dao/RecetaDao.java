package com.recipebook.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.recipebook.logic.Receta;
import com.recipebook.logic.steps.Paso;
import com.recipebook.logic.steps.PasoWextras;

@SuppressWarnings("CallToPrintStackTrace")
public class RecetaDao {
    private final SQLController sqlController;

    public RecetaDao(SQLController sqlController) {
        this.sqlController = sqlController;
    }

    /**
     * Inserta una URL en la tabla MULTIMEDIA y retorna el ID generado.
     * 
     * @param url La URL de la imagen.
     * @return El ID de la multimedia insertada, o null si falla.
     */
    private Integer insertarMultimedia(String url) {
        try {
            String insertMultimediaQuery = String.format(
                "INSERT INTO multimedia (url) VALUES ('%s') RETURNING id_multimedia",
                escapeSQLString(url)
            );
            ResultSet rs = sqlController.executeQuery(insertMultimediaQuery);
            if (rs.next()) {
                return rs.getInt("id_multimedia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Agrega una receta a la base de datos.
     * 
     * @param receta Receta a agregar.
     * @param userID ID del usuario al que pertenece la receta.
     * @return true si la receta se agregó correctamente, false en caso contrario.
     */
    public boolean agregarReceta(Receta receta, int userID) {
        // Insertar imagen de la receta en MULTIMEDIA si existe
        Integer idImagenReceta = null;
        if (receta.getImagen() != null && !receta.getImagen().isEmpty()) {
            idImagenReceta = insertarMultimedia(receta.getImagen());
        }

        try {
            String insertRecetaQuery = String.format(
                "INSERT INTO recetas (nombre_receta, descripcion, tiempo_preparacion, id_usuario, id_imagen) " +
                "VALUES ('%s', %s, %d, %d, %s) RETURNING id_receta",
                escapeSQLString(receta.getNombre()),
                receta.getDescripcion() != null && !receta.getDescripcion().isEmpty() ? 
                    "'" + escapeSQLString(receta.getDescripcion()) + "'" : "NULL",
                receta.getTiempo(),
                userID,
                idImagenReceta != null ? idImagenReceta.toString() : "NULL"
            );

            ResultSet rs = sqlController.executeQuery(insertRecetaQuery);
            int recetaID = -1;
            if (rs.next()) {
                recetaID = rs.getInt("id_receta");
            }

            if (recetaID > 0) {
                // Agregar clasificación (tipos de receta)
                if (receta.getTipo() != null && !receta.getTipo().isEmpty()) {
                    agregarClasificacion(recetaID, receta.getTipo());
                }

                // Insertar pasos
                for (Paso paso : receta.getPasos()) {
                    // Insertar imagen del paso en MULTIMEDIA si existe
                    Integer idImagenPaso = null;
                    if (paso.getImagen() != null && !paso.getImagen().isEmpty()) {
                        idImagenPaso = insertarMultimedia(paso.getImagen());
                    }

                    String insertPasoQuery = String.format(
                        "INSERT INTO pasos (id_receta, descripcion, id_multimedia) " +
                        "VALUES (%d, '%s', %s) RETURNING id_paso",
                        recetaID, escapeSQLString(paso.getDescripcion()),
                        idImagenPaso != null ? idImagenPaso.toString() : "NULL"
                    );
                    
                    ResultSet rsPaso = sqlController.executeQuery(insertPasoQuery);
                    int pasoID = -1;
                    if (rsPaso.next()) {
                        pasoID = rsPaso.getInt("id_paso");
                    }

                    if (pasoID > 0) {
                        // Si el paso tiene extras (es PasoWextras)
                        if (paso instanceof PasoWextras pasoWextras) {
                            // Insertar utensilios para este paso
                            for (String utensilio : pasoWextras.getUtensilios()) {
                                String insertUtensilioQuery = String.format(
                                    "INSERT INTO utensilios (id_receta, id_paso, nombre_utensilio) VALUES (%d, %d, '%s')",
                                    recetaID, pasoID, escapeSQLString(utensilio)
                                );
                                sqlController.executeUpdate(insertUtensilioQuery);
                            }

                            // Insertar ingredientes para este paso
                            for (String ingrediente : pasoWextras.getIngredientes()) {
                                String insertIngredienteQuery = String.format(
                                    "INSERT INTO ingredientes (id_receta, id_paso, nombre_ingrediente) VALUES (%d, %d, '%s')",
                                    recetaID, pasoID, escapeSQLString(ingrediente)
                                );
                                sqlController.executeUpdate(insertIngredienteQuery);
                            }
                        }
                    }
                }

                // Insertar utensilios generales (no asociados a pasos específicos)
                for (String utensilio : receta.getUtensilios()) {
                    String insertUtensilioQuery = String.format(
                        "INSERT INTO utensilios (id_receta, id_paso, nombre_utensilio) VALUES (%d, NULL, '%s')",
                        recetaID, escapeSQLString(utensilio)
                    );
                    sqlController.executeUpdate(insertUtensilioQuery);
                }

                // Insertar ingredientes generales (no asociados a pasos específicos)
                for (String ingrediente : receta.getIngredientes()) {
                    String insertIngredienteQuery = String.format(
                        "INSERT INTO ingredientes (id_receta, id_paso, nombre_ingrediente) VALUES (%d, NULL, '%s')",
                        recetaID, escapeSQLString(ingrediente)
                    );
                    sqlController.executeUpdate(insertIngredienteQuery);
                }

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Agrega clasificación (tipo de receta) a una receta.
     */
    private void agregarClasificacion(int idReceta, String tipoReceta) {
        try {
            // Primero, obtener el ID del tipo de receta
            String selectTipoQuery = String.format(
                "SELECT id_tipo_receta FROM tipo_receta WHERE nombre_tipo = '%s'",
                escapeSQLString(tipoReceta)
            );
            ResultSet rs = sqlController.executeQuery(selectTipoQuery);
            
            if (rs.next()) {
                int idTipoReceta = rs.getInt("id_tipo_receta");
                String insertClasificacionQuery = String.format(
                    "INSERT INTO clasificacion (id_receta, id_tipo_receta) VALUES (%d, %d)",
                    idReceta, idTipoReceta
                );
                sqlController.executeUpdate(insertClasificacionQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene todas las recetas de la base de datos.
     */
    public ArrayList<Receta> obtenerRecetas() {
        ArrayList<Receta> recetas = new ArrayList<>();
        String selectRecetasQuery = "SELECT * FROM recetas";

        try {
            ResultSet rs = sqlController.executeQuery(selectRecetasQuery);
            while (rs.next()) {
                Receta receta = mapResultSetToReceta(rs);
                if (receta != null) {
                    recetas.add(receta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetas;
    }

    /**
     * Obtiene recetas por tipo.
     */
    public ArrayList<Receta> obtenerRecetasPorTipo(String tipo) {
        ArrayList<Receta> recetas = new ArrayList<>();
        String selectRecetasQuery = String.format(
            "SELECT r.* FROM recetas r " +
            "JOIN clasificacion c ON r.id_receta = c.id_receta " +
            "JOIN tipo_receta t ON c.id_tipo_receta = t.id_tipo_receta " +
            "WHERE t.nombre_tipo = '%s'",
            escapeSQLString(tipo)
        );

        try {
            ResultSet rs = sqlController.executeQuery(selectRecetasQuery);
            while (rs.next()) {
                Receta receta = mapResultSetToReceta(rs);
                if (receta != null) {
                    recetas.add(receta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetas;
    }

    /**
     * Obtiene recetas por usuario.
     */
    public ArrayList<Receta> obtenerRecetasPorUsuario(int userID) {
        ArrayList<Receta> recetas = new ArrayList<>();
        String selectRecetasQuery = String.format(
            "SELECT * FROM recetas WHERE id_usuario = %d", userID
        );

        try {
            ResultSet rs = sqlController.executeQuery(selectRecetasQuery);
            while (rs.next()) {
                Receta receta = mapResultSetToReceta(rs);
                if (receta != null) {
                    recetas.add(receta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetas;
    }

    /**
     * Obtiene una receta específica por su ID.
     */
    public Receta obtenerReceta(int recetaID) {
        String selectRecetaQuery = String.format(
            "SELECT * FROM recetas WHERE id_receta = %d", recetaID
        );

        try {
            ResultSet rs = sqlController.executeQuery(selectRecetaQuery);
            if (rs.next()) {
                return mapResultSetToReceta(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Mapea un ResultSet a un objeto Receta.
     */
    private Receta mapResultSetToReceta(ResultSet rs) throws SQLException {
        int recetaID = rs.getInt("id_receta");
        String nombre = rs.getString("nombre_receta");
        String descripcion = rs.getString("descripcion");
        int tiempo = rs.getInt("tiempo_preparacion");
        int idImagen = rs.getInt("id_imagen");
        String urlImagen = obtenerImagenReceta(idImagen);
        String tipo = obtenerTipoReceta(recetaID);

        Receta receta = new Receta(nombre, urlImagen, descripcion, tipo);
        receta.setTiempo(tiempo);

        // Obtener valoración promedio y descripción
        obtenerValoracionReceta(recetaID, receta);

        // Obtener pasos
        String selectPasosQuery = String.format("SELECT * FROM pasos WHERE id_receta = %d", recetaID);
        try {
            ResultSet rsPasos = sqlController.executeQuery(selectPasosQuery);
            while (rsPasos.next()) {
                int pasoID = rsPasos.getInt("id_paso");
                String pasoDescripcion = rsPasos.getString("descripcion");
                int idMultimediaPaso = rsPasos.getInt("id_multimedia");
                String pasoImagen = obtenerImagenReceta(idMultimediaPaso);

                // Obtener ingredientes y utensilios del paso
                List<String> ingredientesPaso = obtenerIngredientesPorPaso(pasoID);
                List<String> utensiliosPaso = obtenerUtensiliosPorPaso(pasoID);

                if (!ingredientesPaso.isEmpty() || !utensiliosPaso.isEmpty()) {
                    receta.addStep(pasoDescripcion, 0, 
                        utensiliosPaso.toArray(new String[0]), 
                        ingredientesPaso.toArray(new String[0]), 
                        pasoImagen);
                } else {
                    receta.addStep(pasoDescripcion, 0, pasoImagen);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Obtener ingredientes generales
        String selectIngredientesQuery = String.format(
            "SELECT * FROM ingredientes WHERE id_receta = %d AND id_paso IS NULL", recetaID
        );
        try {
            ResultSet rsIngredientes = sqlController.executeQuery(selectIngredientesQuery);
            while (rsIngredientes.next()) {
                String ingrediente = rsIngredientes.getString("nombre_ingrediente");
                receta.addIngrediente(ingrediente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Obtener utensilios generales
        String selectUtensiliosQuery = String.format(
            "SELECT * FROM utensilios WHERE id_receta = %d AND id_paso IS NULL", recetaID
        );
        try {
            ResultSet rsUtensilios = sqlController.executeQuery(selectUtensiliosQuery);
            while (rsUtensilios.next()) {
                String utensilio = rsUtensilios.getString("nombre_utensilio");
                receta.addUtensilio(utensilio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return receta;
    }

    /**
     * Obtiene el tipo de receta por su ID.
     */
    private String obtenerTipoReceta(int idReceta) {
        try {
            String selectTipoQuery = String.format(
                "SELECT t.nombre_tipo FROM tipo_receta t " +
                "JOIN clasificacion c ON t.id_tipo_receta = c.id_tipo_receta " +
                "WHERE c.id_receta = %d LIMIT 1", idReceta
            );
            ResultSet rs = sqlController.executeQuery(selectTipoQuery);
            if (rs.next()) {
                return rs.getString("nombre_tipo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene la URL de la imagen/multimedia.
     */
    private String obtenerImagenReceta(int idMultimedia) {
        if (idMultimedia <= 0) {
            return null;
        }

        try {
            String selectImagenQuery = String.format(
                "SELECT url FROM multimedia WHERE id_multimedia = %d", idMultimedia
            );
            ResultSet rs = sqlController.executeQuery(selectImagenQuery);
            if (rs.next()) {
                return rs.getString("url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene ingredientes de un paso específico.
     */
    private List<String> obtenerIngredientesPorPaso(int idPaso) {
        List<String> ingredientes = new ArrayList<>();
        try {
            String selectQuery = String.format(
                "SELECT nombre_ingrediente FROM ingredientes WHERE id_paso = %d", idPaso
            );
            ResultSet rs = sqlController.executeQuery(selectQuery);
            while (rs.next()) {
                ingredientes.add(rs.getString("nombre_ingrediente"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredientes;
    }

    /**
     * Obtiene utensilios de un paso específico.
     */
    private List<String> obtenerUtensiliosPorPaso(int idPaso) {
        List<String> utensilios = new ArrayList<>();
        try {
            String selectQuery = String.format(
                "SELECT nombre_utensilio FROM utensilios WHERE id_paso = %d", idPaso
            );
            ResultSet rs = sqlController.executeQuery(selectQuery);
            while (rs.next()) {
                utensilios.add(rs.getString("nombre_utensilio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utensilios;
    }

    /**
     * Obtiene la valoración promedio y descripción de una receta.
     */
    private void obtenerValoracionReceta(int idReceta, Receta receta) {
        try {
            // Obtener promedio de valoración
            String selectPromedioQuery = String.format(
                "SELECT AVG(valor) as promedio_valor FROM valoracion WHERE id_receta = %d AND valor IS NOT NULL",
                idReceta
            );
            ResultSet rsPromedio = sqlController.executeQuery(selectPromedioQuery);
            if (rsPromedio.next()) {
                double promedio = rsPromedio.getDouble("promedio_valor");
                if (!Double.isNaN(promedio)) {
                    receta.setValoracion(promedio);
                }
            }

            // Obtener descripción del comentario más reciente
            String selectDescripcionQuery = String.format(
                "SELECT comentario FROM valoracion WHERE id_receta = %d AND comentario IS NOT NULL ORDER BY fecha_comentario DESC LIMIT 1",
                idReceta
            );
            ResultSet rsDescripcion = sqlController.executeQuery(selectDescripcionQuery);
            if (rsDescripcion.next()) {
                String descripcion = rsDescripcion.getString("comentario");
                if (descripcion != null && !descripcion.isEmpty()) {
                    receta.setDescripcionValoracion(descripcion);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escapa caracteres especiales en strings para SQL.
     */
    private String escapeSQLString(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("'", "''");
    }

    /**
     * Elimina una receta de un usuario específico.
     * 
     * @param recetaID ID de la receta a eliminar.
     * @param userID ID del usuario al que pertenece la receta.
     * @return true si la receta se eliminó correctamente, false en caso contrario.
     */
    public boolean eliminarReceta(int recetaID, int userID) {
        try {
            // Eliminar referencias en Pasos
            String deletePasosQuery = String.format("DELETE FROM pasos WHERE id_receta = %d", recetaID);
            sqlController.executeUpdate(deletePasosQuery);

            // Eliminar referencias en Utensilios
            String deleteUtensiliosQuery = String.format("DELETE FROM utensilios WHERE id_receta = %d", recetaID);
            sqlController.executeUpdate(deleteUtensiliosQuery);

            // Eliminar referencias en Ingredientes
            String deleteIngredientesQuery = String.format("DELETE FROM ingredientes WHERE id_receta = %d", recetaID);
            sqlController.executeUpdate(deleteIngredientesQuery);

            // Eliminar la receta
            String deleteRecetaQuery = String.format("DELETE FROM recetas WHERE id_receta = %d AND id_usuario = %d", recetaID, userID);
            int affectedRows = sqlController.executeUpdate(deleteRecetaQuery);

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina todas las recetas de un usuario específico.
     * 
     * @param userID ID del usuario.
     * @return true si las recetas se eliminaron correctamente, false en caso contrario.
     */
    public boolean eliminarTodasRecetasPorUsuario(int userID) {
        try {
            // Obtener todas las recetas del usuario
            String selectRecetasQuery = String.format("SELECT id_receta FROM recetas WHERE id_usuario = %d", userID);
            ResultSet rs = sqlController.executeQuery(selectRecetasQuery);

            // Eliminar referencias de cada receta
            while (rs.next()) {
                int recetaID = rs.getInt("id_receta");

                // Eliminar referencias en Pasos
                String deletePasosQuery = String.format("DELETE FROM pasos WHERE id_receta = %d", recetaID);
                sqlController.executeUpdate(deletePasosQuery);

                // Eliminar referencias en Utensilios
                String deleteUtensiliosQuery = String.format("DELETE FROM utensilios WHERE id_receta = %d", recetaID);
                sqlController.executeUpdate(deleteUtensiliosQuery);

                // Eliminar referencias en Ingredientes
                String deleteIngredientesQuery = String.format("DELETE FROM ingredientes WHERE id_receta = %d", recetaID);
                sqlController.executeUpdate(deleteIngredientesQuery);
            }

            // Eliminar todas las recetas del usuario
            String deleteRecetasQuery = String.format("DELETE FROM recetas WHERE id_usuario = %d", userID);
            int affectedRows = sqlController.executeUpdate(deleteRecetasQuery);

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}