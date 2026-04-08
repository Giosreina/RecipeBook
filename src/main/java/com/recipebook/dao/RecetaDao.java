package com.recipebook.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
     * @param tipo El tipo de multimedia (ej: 'imagen').
     * @return El ID de la multimedia insertada, o null si falla.
     */
    private Integer insertarMultimedia(String url) {
        try {
            String insertMultimediaQuery = String.format(
                "INSERT INTO MULTIMEDIA (tipo_multimedia, url) VALUES ('%s')",
                 url
            );
            sqlController.executeUpdate(insertMultimediaQuery);
            ResultSet rs = sqlController.executeQuery("SELECT SCOPE_IDENTITY() AS MultimediaID");
            rs.next();
            return rs.getInt("MultimediaID");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
        if (receta.getImagen() != null) {
            idImagenReceta = insertarMultimedia(receta.getImagen());
            if (idImagenReceta == null) {
                return false; // Falló la inserción de multimedia
            }
        }

        String insertRecetaQuery = String.format(
            "INSERT INTO RECETAS(id_usuario, nombre_receta, id_tipo_receta, id_imagen, descripcion, tiempo_preparacion) VALUES (%d, '%s', %s, %s, %s, %d)",
            userID, 
            receta.getNombre(), 
            receta.getTipo() != null ? "'" + receta.getTipo() + "'" : "NULL",
            idImagenReceta != null ? idImagenReceta.toString() : "NULL",
            receta.getDescripcion() != null ? "'" + receta.getDescripcion() + "'" : "NULL",
            receta.getTiempo()
        );

        try {
            sqlController.executeUpdate(insertRecetaQuery);
            ResultSet rs = sqlController.executeQuery("SELECT SCOPE_IDENTITY() AS RecetaID");
            rs.next();
            int recetaID = rs.getInt("RecetaID");

            // Insert steps and get their IDs
            for (Paso paso : receta.getPasos()) {
                // Insertar imagen del paso en MULTIMEDIA si existe
                Integer idImagenPaso = null;
                if (paso.getImagen() != null ) {
                    idImagenPaso = insertarMultimedia(paso.getImagen());
                    if (idImagenPaso == null) {
                        return false; // Falló la inserción de multimedia
                    }
                }

                String insertPasoQuery = String.format(
                    "INSERT INTO PASOS (id_receta, descripcion, id_multimedia) VALUES (%d, '%s', %s)",
                    recetaID, paso.getDescripcion(), 
                    idImagenPaso != null ? idImagenPaso.toString() : "NULL"
                );
                sqlController.executeUpdate(insertPasoQuery);
                
                // Get the PasoID
                ResultSet rsPaso = sqlController.executeQuery("SELECT SCOPE_IDENTITY() AS id_paso");
                rsPaso.next();
                int pasoID = rsPaso.getInt("id_paso");

                // If the step has extras (is PasoWextras)
                if (paso instanceof PasoWextras pasoWextras) {
                    
                    // Insert utensilios for this step
                    for (String utensilio : pasoWextras.getUtensilios()) {
                    String insertUtensilioQuery = String.format(
                        "INSERT INTO UTENSILIOS (id_receta, id_paso, nombre_utensilio) VALUES (%d, %d, '%s')",
                        recetaID, pasoID, utensilio
                    );
                    sqlController.executeUpdate(insertUtensilioQuery);
                    }

                    // Insert ingredientes for this step
                    for (String ingrediente : pasoWextras.getIngredientes()) {
                    String insertIngredienteQuery = String.format(
                        "INSERT INTO INGREDIENTES (id_receta, id_paso, nombre_ingrediente) VALUES (%d, %d, '%s')",
                        recetaID, pasoID, ingrediente
                    );
                    sqlController.executeUpdate(insertIngredienteQuery);
                    }
                }
            }

            // Insert general utensilios (not associated with specific steps)
            for (String utensilio : receta.getUtensilios()) {
            String insertUtensilioQuery = String.format(
                "INSERT INTO UTENSILIOS (id_receta, id_paso, nombre_utensilio) VALUES (%d, NULL, '%s')",
                recetaID, utensilio
            );
            sqlController.executeUpdate(insertUtensilioQuery);
            }

            // Insert general ingredientes (not associated with specific steps)
            for (String ingrediente : receta.getIngredientes()) {
            String insertIngredienteQuery = String.format(
                "INSERT INTO INGREDIENTES (id_receta, id_paso, nombre_ingrediente) VALUES (%d, NULL, '%s')",
                recetaID, ingrediente
            );
            sqlController.executeUpdate(insertIngredienteQuery);
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene todas las recetas de la base de datos.
     * 
     * @return Contenedor de recetas.
     */
    public ArrayList<Receta> obtenerRecetas() {
        ArrayList<Receta> recetasContainer = new ArrayList<>();
        String selectRecetasQuery = "SELECT * FROM Recetas";

        try {
            ResultSet rs = sqlController.executeQuery(selectRecetasQuery);

            while (rs.next()) {
                Receta receta = mapResultSetToReceta(rs);
                recetasContainer.add(receta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetasContainer;
    }
    private String obtenerTipoReceta(int id_tipo) {
        String selectTipoQuery = String.format("SELECT nombre_tipo FROM TIPO_RECETA WHERE id_tipo_receta = %d", id_tipo);
        try {
            ResultSet rs = sqlController.executeQuery(selectTipoQuery);
            if (rs.next()) {
                return rs.getString("nombre_tipo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // o un valor por defecto
    }
    private String obtenerImagenReceta(int id_imagen) {
        String selectImagenQuery = String.format("SELECT url FROM MULTIMEDIA WHERE id_multimedia = %d", id_imagen);
        try {
            ResultSet rs = sqlController.executeQuery(selectImagenQuery);
            if (rs.next()) {
                return rs.getString("url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // o un valor por defecto
    }

     /**
     * Obtiene una receta específica por su ID.
     * 
     * @param recetaID ID de la receta a obtener.
     * @return Objeto Receta si se encuentra, null en caso contrario.
     */

    /**
     * Obtiene todas las recetas de un usuario específico.
     * 
     * @param userID ID del usuario.
     * @return Contenedor de recetas del usuario.
     */
    public ArrayList<Receta> obtenerRecetasPorUsuario(int userID) {
        ArrayList<Receta> recetasContainer = new ArrayList<>();
        String selectRecetasQuery = String.format("SELECT * FROM Recetas WHERE UserID = %d", userID);

        try {
            ResultSet rs = sqlController.executeQuery(selectRecetasQuery);

            while (rs.next()) {
                Receta receta = mapResultSetToReceta(rs);
                recetasContainer.add(receta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetasContainer;
    }

    /**
     * Obtiene todas las recetas de un tipo específico.
     * 
     * @param tipo Tipo de receta.
     * @return Contenedor de recetas del tipo especificado.
     */
    public ArrayList<Receta> obtenerRecetasPorTipo(String tipo) {
        ArrayList<Receta> recetasContainer = new ArrayList<>();
        String selectRecetasQuery = String.format("SELECT R.* FROM RECETAS R JOIN TIPO_RECETA T ON R.id_tipo_receta = T.id_tipo_receta WHERE T.nombre_tipo = '%s'", tipo);

        try {
            ResultSet rs = sqlController.executeQuery(selectRecetasQuery);

            while (rs.next()) {
                Receta receta = mapResultSetToReceta(rs);
                recetasContainer.add(receta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetasContainer;
    }

    /**
     * Mapea un ResultSet a un objeto Receta.
     * 
     * @param rs ResultSet a mapear.
     * @return Objeto Receta.
     * @throws SQLException Si ocurre un error al mapear el ResultSet.
     */
    private Receta mapResultSetToReceta(ResultSet rs) throws SQLException {
        int recetaID = rs.getInt("RecetaID");
        String nombre = rs.getString("Nombre");
        String tipo = obtenerTipoReceta(rs.getInt("Tipo"));
        String imagen = obtenerImagenReceta(rs.getInt("id_imagen"));
        String descripcion = rs.getString("Descripcion");
        int tiempo = rs.getInt("Tiempo");
        double valor = rs.getDouble("Valoracion");

        Receta receta = new Receta(nombre, imagen, descripcion, tipo);
        receta.setTiempo(tiempo);
        receta.setValoracion(valor);

        // Obtener pasos
        String selectPasosQuery = String.format("SELECT * FROM PASOS WHERE id_receta = %d", recetaID);
        ResultSet rsPasos = sqlController.executeQuery(selectPasosQuery);
        while (rsPasos.next()) {
            int pasoID = rsPasos.getInt("id_paso");
            String pasoDescripcion = rsPasos.getString("descripcion");
            int pasoTiempo = rsPasos.getInt("tiempo");
            String pasoImagen = obtenerImagenReceta(rsPasos.getInt("id_multimedia"));

            Paso paso = new Paso(pasoID, pasoDescripcion, pasoTiempo, pasoImagen);
            receta.getPasos().add(paso);
        }

        // Obtener utensilios
        String selectUtensiliosQuery = String.format("SELECT * FROM Utensilios WHERE id_receta = %d", recetaID);
        ResultSet rsUtensilios = sqlController.executeQuery(selectUtensiliosQuery);
        while (rsUtensilios.next()) {
            String utensilio = rsUtensilios.getString("nombre_utensilio");
            receta.getUtensilios().add(utensilio);
        }

        // Obtener ingredientes
        String selectIngredientesQuery = String.format("SELECT * FROM Ingredientes WHERE id_receta = %d", recetaID);
        ResultSet rsIngredientes = sqlController.executeQuery(selectIngredientesQuery);
        while (rsIngredientes.next()) {
            String ingrediente = rsIngredientes.getString("nombre_ingrediente");
            receta.getIngredientes().add(ingrediente);
        }

        return receta;
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
            String deletePasosQuery = String.format("DELETE FROM PASOS WHERE id_receta = %d", recetaID);
            sqlController.executeUpdate(deletePasosQuery);

            // Eliminar referencias en Utensilios
            String deleteUtensiliosQuery = String.format("DELETE FROM UTENSILIOS WHERE id_receta = %d", recetaID);
            sqlController.executeUpdate(deleteUtensiliosQuery);

            // Eliminar referencias en Ingredientes
            String deleteIngredientesQuery = String.format("DELETE FROM INGREDIENTES WHERE id_receta = %d", recetaID);
            sqlController.executeUpdate(deleteIngredientesQuery);

            // Eliminar la receta
            String deleteRecetaQuery = String.format("DELETE FROM RECETAS WHERE id_receta = %d AND id_usuario = %d", recetaID, userID);
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
            String selectRecetasQuery = String.format("SELECT id_receta FROM RECETAS WHERE id_usuario = %d", userID);
            ResultSet rs = sqlController.executeQuery(selectRecetasQuery);

            // Eliminar referencias de cada receta
            while (rs.next()) {
                int recetaID = rs.getInt("id_receta");

                // Eliminar referencias en Pasos
                String deletePasosQuery = String.format("DELETE FROM PASOS WHERE id_receta = %d", recetaID);
                sqlController.executeUpdate(deletePasosQuery);

                // Eliminar referencias en Utensilios
                String deleteUtensiliosQuery = String.format("DELETE FROM UTENSILIOS WHERE id_receta = %d", recetaID);
                sqlController.executeUpdate(deleteUtensiliosQuery);

                // Eliminar referencias en Ingredientes
                String deleteIngredientesQuery = String.format("DELETE FROM INGREDIENTES WHERE id_receta = %d", recetaID);
                sqlController.executeUpdate(deleteIngredientesQuery);
            }

            // Eliminar todas las recetas del usuario
            String deleteRecetasQuery = String.format("DELETE FROM RECETAS WHERE id_usuario = %d", userID);
            int affectedRows = sqlController.executeUpdate(deleteRecetasQuery);

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}